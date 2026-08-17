package com.harvey.ai.service;

import cn.hutool.core.lang.UUID;
import com.alibaba.fastjson2.JSON;
import com.harvey.ai.config.ChatModelFactory;
import com.harvey.ai.model.entity.AiFaq;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智能客服服务: 以 FAQ + 知识库为严格知识源, 无法解答时标记转人工
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiCustomerService {

    private static final Pattern CITE_PATTERN = Pattern.compile("\\[(\\d+)\\]");
    private static final String NEED_HUMAN_MARKER = "[无法回答]";
    private static final int FAQ_TOP_K = 2;
    private static final int KB_TOP_K = 3;

    private final ChatModelFactory chatModelFactory;
    private final RetrievalService retrievalService;
    private final AiFaqService faqService;

    public CustomerChatResult chat(Long modelId, String conversationId, String message, List<Long> kbIds) {
        String cid = normalizeConversationId(conversationId);
        ChatClient chatClient = chatModelFactory.getChatClient(modelId);
        List<KnowledgeItem> items = retrieveKnowledge(message, kbIds);
        String raw = chatClient.prompt()
                .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, cid))
                .system(buildCustomerPrompt(items))
                .user(message)
                .call()
                .content();
        boolean needHuman = stripNeedHuman(raw);
        String content = needHuman ? raw.replace(NEED_HUMAN_MARKER, "").trim() : raw;
        List<CustomerCitation> citations = parseCitations(raw, items);
        incrCitedFaqs(citations);
        return new CustomerChatResult(cid, content, citations, needHuman);
    }

    public Flux<ServerSentEvent<String>> streamChat(Long modelId, String conversationId, String message, List<Long> kbIds) {
        String cid = normalizeConversationId(conversationId);
        ChatClient chatClient = chatModelFactory.getChatClient(modelId);
        List<KnowledgeItem> items = retrieveKnowledge(message, kbIds);
        StringBuilder acc = new StringBuilder();

        return chatClient.prompt()
                .advisors(a -> a.param(MessageChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY, cid))
                .system(buildCustomerPrompt(items))
                .user(message)
                .stream()
                .content()
                .doOnNext(acc::append)
                .map(chunk -> ServerSentEvent.<String>builder(chunk).event("message").build())
                .concatWith(Flux.defer(() -> {
                    String full = acc.toString();
                    boolean needHuman = stripNeedHuman(full);
                    List<CustomerCitation> citations = parseCitations(full, items);
                    incrCitedFaqs(citations);
                    return Flux.concat(
                            Flux.just(ServerSentEvent.<String>builder(JSON.toJSONString(citations)).event("citation").build()),
                            Flux.just(ServerSentEvent.<String>builder(String.valueOf(needHuman)).event("flag").build()),
                            Flux.just(ServerSentEvent.<String>builder("[DONE]").build()));
                }))
                .onErrorResume(e -> {
                    log.error("智能客服流式对话异常", e);
                    return Flux.just(ServerSentEvent.<String>builder("Error: " + e.getMessage()).event("error").build());
                });
    }

    /**
     * 检索 FAQ(加权) + 知识库分块, 合并为统一知识项列表
     */
    private List<KnowledgeItem> retrieveKnowledge(String message, List<Long> kbIds) {
        List<KnowledgeItem> items = new ArrayList<>();
        for (AiFaqService.RetrievedFaq hit : faqService.retrieve(message, FAQ_TOP_K)) {
            AiFaq faq = hit.faq();
            items.add(new KnowledgeItem("FAQ", faq.getId(), null, faq.getQuestion(), null, faq.getAnswer()));
        }
        for (RetrievalService.RetrievedChunkResult hit : retrievalService.retrieve(kbIds, message, KB_TOP_K)) {
            items.add(new KnowledgeItem("DOC", null, hit.chunk().getDocId(),
                    StringUtils.hasText(hit.fileName()) ? hit.fileName() : "未知文档",
                    hit.chunk().getChunkIndex(), hit.chunk().getContent()));
        }
        return items;
    }

    /**
     * 构建客服系统提示词(始终携带客服人设; 无知识时引导转人工)
     */
    private String buildCustomerPrompt(List<KnowledgeItem> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("你是企业智能客服小Y，负责解答客户关于公司产品、服务、政策等方面的问题。\n");
        sb.append("回答规则：\n");
        sb.append("1. 只能依据「参考知识」回答，不要编造不存在的信息；\n");
        sb.append("2. 引用知识时在句末标注来源编号，格式如 [1][2]；\n");
        sb.append("3. 若参考知识无法回答用户问题，回答必须以「[无法回答]」开头，再简要说明原因；\n");
        sb.append("4. 语气礼貌专业，使用中文简洁回答。\n");
        if (items != null && !items.isEmpty()) {
            sb.append("\n参考知识：\n");
            for (int i = 0; i < items.size(); i++) {
                KnowledgeItem item = items.get(i);
                sb.append('[').append(i + 1).append("] 来源:《").append(item.title()).append("》\n");
                sb.append(item.content()).append("\n\n");
            }
        } else {
            sb.append("\n(当前无参考知识, 无法回答时按规则3处理)\n");
        }
        return sb.toString();
    }

    private List<CustomerCitation> parseCitations(String answer, List<KnowledgeItem> items) {
        if (items == null || items.isEmpty() || !StringUtils.hasText(answer)) {
            return List.of();
        }
        List<CustomerCitation> citations = new ArrayList<>();
        Matcher matcher = CITE_PATTERN.matcher(answer);
        while (matcher.find()) {
            int idx = Integer.parseInt(matcher.group(1)) - 1;
            if (idx < 0 || idx >= items.size()) {
                continue;
            }
            KnowledgeItem item = items.get(idx);
            CustomerCitation citation = new CustomerCitation(item.source(), item.faqId(), item.docId(),
                    item.title(), item.chunkIndex(), item.content());
            if (!citations.contains(citation)) {
                citations.add(citation);
            }
        }
        return citations;
    }

    private void incrCitedFaqs(List<CustomerCitation> citations) {
        Set<Long> ids = new HashSet<>();
        for (CustomerCitation citation : citations) {
            if (citation.source().equals("FAQ") && citation.faqId() != null) {
                ids.add(citation.faqId());
            }
        }
        ids.forEach(faqService::incrHitCount);
    }

    private boolean stripNeedHuman(String content) {
        return content != null && content.contains(NEED_HUMAN_MARKER);
    }

    private String normalizeConversationId(String conversationId) {
        return conversationId == null || conversationId.isBlank() ? UUID.fastUUID().toString() : conversationId;
    }

    /**
     * 统一知识项: FAQ 答案或知识库分块
     */
    public record KnowledgeItem(String source, Long faqId, Long docId, String title,
                                Integer chunkIndex, String content) {
    }

    public record CustomerCitation(String source, Long faqId, Long docId, String title,
                                   Integer chunkIndex, String content) {
    }

    public record CustomerChatResult(String conversationId, String content,
                                     List<CustomerCitation> citations, boolean needHuman) {
    }
}