package com.harvey.ai.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * AI模型配置表
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_model_config")
@Schema(title = "AiModelConfig对象", description = "AI模型配置表")
public class AiModelConfig extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "modelName", description = "模型展示名称")
    private String modelName;

    @Schema(title = "provider", description = "厂商: deepseek/qwen/glm/kimi/ollama/other")
    private String provider;

    @Schema(title = "model", description = "模型标识, 如 deepseek-chat")
    private String model;

    @Schema(title = "baseUrl", description = "接口地址")
    private String baseUrl;

    @Schema(title = "apiKey", description = "API密钥(AES加密存储)")
    private String apiKey;

    @Schema(title = "isDefault", description = "是否默认模型(0否,1是)")
    private Integer isDefault;

}