package com.harvey.ai.config;

import com.harvey.ai.model.entity.AiModelConfig;
import com.harvey.ai.service.AiModelConfigService;
import com.harvey.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.context.event.EventListener;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态模型工厂: 根据 ai_model_config 配置动态创建 ChatClient, 后台切换模型无需重启
 *
 * @author harvey
 * @since 2026-08-17
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatModelFactory {

    private static final String SYSTEM_PROMPT = "你是一个智能办公助手，你的名字叫小Y。请使用中文简洁准确地回答用户问题。";

    private final AiModelConfigService modelConfigService;
    private final ChatMemory chatMemory;
    /** spring.ai.openai.* 静态配置的模型(可选), 用于数据库无配置时的兜底 */
    private final ObjectProvider<OpenAiChatModel> fallbackChatModelProvider;

    private final Map<Long, ChatClient> chatClientCache = new ConcurrentHashMap<>();

    /**
     * 获取指定模型配置对应的 ChatClient; modelId 为空时使用默认模型
     */
    public ChatClient getChatClient(Long modelId) {
        AiModelConfig config;
        if (modelId != null) {
            config = modelConfigService.getById(modelId);
            if (config == null) {
                throw new BusinessException("模型配置不存在");
            }
        } else {
            config = modelConfigService.getDefaultModel();
            if (config == null) {
                OpenAiChatModel fallback = fallbackChatModelProvider.getIfAvailable();
                if (fallback == null) {
                    throw new BusinessException("未配置默认模型, 请先在「AI 智能-模型管理」中启用并设置默认模型");
                }
                log.warn("数据库无默认模型配置, 使用 spring.ai.openai 静态配置兜底");
                return buildChatClient(fallback);
            }
        }
        if (config.getEnabled() == null || config.getEnabled() != 1) {
            throw new BusinessException("模型【" + config.getModelName() + "】已被禁用");
        }
        return chatClientCache.computeIfAbsent(config.getId(), id -> {
            OpenAiApi openAiApi = new OpenAiApi(config.getBaseUrl(), modelConfigService.decryptApiKey(config.getApiKey()));
            OpenAiChatOptions options = OpenAiChatOptions.builder().model(config.getModel()).build();
            OpenAiChatModel chatModel = new OpenAiChatModel(openAiApi, options);
            log.info("创建模型 ChatClient: {} ({})", config.getModelName(), config.getModel());
            return buildChatClient(chatModel);
        });
    }

    private ChatClient buildChatClient(OpenAiChatModel chatModel) {
        return ChatClient.builder(chatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(new MessageChatMemoryAdvisor(chatMemory,
                        MessageChatMemoryAdvisor.DEFAULT_CHAT_MEMORY_CONVERSATION_ID, 20))
                .build();
    }

    /**
     * 配置变更后失效对应缓存
     */
    @EventListener
    public void onModelConfigChanged(ModelConfigChangedEvent event) {
        ChatClient removed = chatClientCache.remove(event.modelId());
        if (removed != null) {
            log.info("模型配置变更, 已失效缓存: {}", event.modelId());
        }
    }
}