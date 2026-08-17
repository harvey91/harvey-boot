package com.harvey.ai.config;

/**
 * 模型配置变更事件, 用于失效 ChatClient 缓存
 *
 * @author harvey
 * @since 2026-08-17
 */
public record ModelConfigChangedEvent(Long modelId) {
}