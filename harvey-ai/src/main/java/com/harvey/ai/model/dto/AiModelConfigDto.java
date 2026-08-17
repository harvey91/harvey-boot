package com.harvey.ai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiModelConfigDto")
public class AiModelConfigDto {

    private Long id;

    @NotBlank(message = "模型名称不能为空")
    @Schema(title = "modelName", description = "模型展示名称")
    private String modelName;

    @NotBlank(message = "厂商不能为空")
    @Schema(title = "provider", description = "厂商: deepseek/qwen/glm/kimi/ollama/other")
    private String provider;

    @NotBlank(message = "模型标识不能为空")
    @Schema(title = "model", description = "模型标识, 如 deepseek-chat")
    private String model;

    @NotBlank(message = "接口地址不能为空")
    @Schema(title = "baseUrl", description = "接口地址")
    private String baseUrl;

    @Schema(title = "apiKey", description = "API密钥, 修改时留空表示不修改")
    private String apiKey;

    @NotNull(message = "是否默认不能为空")
    @Schema(title = "isDefault", description = "是否默认模型(0否,1是)")
    private Integer isDefault;

    @Schema(title = "enabled", description = "是否启用(0禁用,1启用)", defaultValue = "1")
    private Integer enabled;

    @Schema(title = "sort", description = "排序")
    private Integer sort;

    @Schema(title = "remark", description = "备注")
    private String remark;
}