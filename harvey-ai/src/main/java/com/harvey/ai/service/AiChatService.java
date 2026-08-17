package com.harvey.ai.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.harvey.ai.config.ChatModelFactory;
import com.harvey.ai.rag.RetrievalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * AI 对话服务(支持知识库检索增强 RAG)
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatService {

    private static final Pattern CITE_PATTERN = Pattern.compile("\\[(\\d+)\\]");
    private static final int TOP_K = 4;

    private final ChatModelFactory chatModelFactory;
    private final RetrievalService retrievalService;

    /**
     * 普通对话(多轮记忆)
     */
    public ChatResult chat(Long modelId, String conversationId, String message) {
        return chat(modelId, conversationId, message, null, false);
    }

    /**
     * 对话(可选知识库检索增强)
     *
     * @param kbIds             检索的知识库ID, 为空检索全部启用知识库
     * @param useKnowledgeBase  是否启用知识库检索
     */
    public ChatResult chat(Long modelId, String conversationId, String message,
                           List<Long> kbIds, boolean useKnowledgeBase) {
        String cid = normalizeConversationId(conversationId);
        ChatClient chatClient = chatModelFactory.getChatClient(modelId);
        List<RetrievalService.RetrievedChunkResult> hits = useKnowledgeBase
                ? retrievalService.retrieve(kbIds, message, TOP_K) : List.of();
        String knowledgePrompt = buildKnowledgePrompt(hits);

        ChatClient.ChatClientRequestSpec spec = chatClient.prompt()
                .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, cid));
        if (StringUtils.hasText(knowledgePrompt)) {
            spec = spec.system(knowledgePrompt);
        }
        String content = spec.user(message).call().content();
        return new ChatResult(cid, content, parseCitations(content, hits));
    }

    /**
     * 流式对话(多轮记忆), 结束时追加 citation 事件与 [DONE]
     */
    public Flux<ServerSentEvent<String>> streamChat(Long modelId, String conversationId, String message) {
        return streamChat(modelId, conversationId, message, null, false);
    }

    public Flux<ServerSentEvent<String>> streamChat(Long modelId, String conversationId, String message,
                                                    List<Long> kbIds, boolean useKnowledgeBase) {
        String cid = normalizeConversationId(conversationId);
        ChatClient chatClient = chatModelFactory.getChatClient(modelId);
        List<RetrievalService.RetrievedChunkResult> hits = useKnowledgeBase
                ? retrievalService.retrieve(kbIds, message, TOP_K) : List.of();
        String knowledgePrompt = buildKnowledgePrompt(hits);
        StringBuilder acc = new StringBuilder();

        ChatClient.ChatClientRequestSpec spec = chatClient.prompt()
                .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, cid));
        if (StringUtils.hasText(knowledgePrompt)) {
            spec = spec.system(knowledgePrompt);
        }
        return spec.user(message)
                .stream()
                .content()
                .doOnNext(acc::append)
                .map(content -> ServerSentEvent.<String>builder(content).event("message").build())
                .concatWith(Flux.defer(() -> {
                    List<Citation> citations = parseCitations(acc.toString(), hits);
                    return Flux.concat(
                            Flux.just(ServerSentEvent.<String>builder(JSON.toJSONString(citations)).event("citation").build()),
                            Flux.just(ServerSentEvent.<String>builder("[DONE]").build()));
                }))
                .onErrorResume(e -> {
                    log.error("流式对话异常", e);
                    return Flux.just(ServerSentEvent.<String>builder("Error: " + e.getMessage()).event("error").build());
                });
    }

    /**
     * 构建知识检索系统提示词; 无检索结果返回 null(使用默认提示词)
     */
    private String buildKnowledgePrompt(List<RetrievalService.RetrievedChunkResult> hits) {
        if (hits == null || hits.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("你是智能办公助手小Y。请依据「参考知识」回答用户问题。\n");
        sb.append("回答规则：\n");
        sb.append("1. 只能依据参考知识回答, 不要编造不存在的内容；\n");
        sb.append("2. 引用参考知识时在句末标注来源编号, 格式如 [1][2]；\n");
        sb.append("3. 若参考知识不足以回答, 请明确说明；\n");
        sb.append("4. 使用中文简洁准确地回答。\n\n");
        sb.append("参考知识：\n");
        for (int i = 0; i < hits.size(); i++) {
            RetrievalService.RetrievedChunkResult hit = hits.get(i);
            String source = StringUtils.hasText(hit.fileName()) ? hit.fileName() : "未知文档";
            sb.append('[').append(i + 1).append("] 来源:《").append(source).append("》\n");
            sb.append(hit.chunk().getContent()).append("\n\n");
        }
        return sb.toString();
    }

    /**
     * 从回答内容中解析引用编号, 映射为引用列表
     */
    private List<Citation> parseCitations(String answer, List<RetrievalService.RetrievedChunkResult> hits) {
        if (hits == null || hits.isEmpty() || !StringUtils.hasText(answer)) {
            return List.of();
        }
        List<Citation> citations = new ArrayList<>();
        Matcher matcher = CITE_PATTERN.matcher(answer);
        while (matcher.find()) {
            int idx = Integer.parseInt(matcher.group(1)) - 1;
            if (idx < 0 || idx >= hits.size()) {
                continue;
            }
            RetrievalService.RetrievedChunkResult hit = hits.get(idx);
            Citation citation = new Citation(hit.chunk().getDocId(), hit.fileName(),
                    hit.chunk().getChunkIndex(), hit.chunk().getContent());
            if (!citations.contains(citation)) {
                citations.add(citation);
            }
        }
        return citations;
    }

    private String normalizeConversationId(String conversationId) {
        return conversationId == null || conversationId.isBlank() ? UUID.fastUUID().toString() : conversationId;
    }

    /**
     * 引用来源
     */
    public record Citation(Long docId, String fileName, Integer chunkIndex, String content) {
    }

    public record ChatResult(String conversationId, String content, List<Citation> citations) {
    }
}