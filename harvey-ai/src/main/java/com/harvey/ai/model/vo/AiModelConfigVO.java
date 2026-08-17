package com.harvey.ai.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiModelConfigVO")
public class AiModelConfigVO {
    private Long id;

    @Schema(title = "modelName", description = "模型展示名称")
    private String modelName;

    @Schema(title = "provider", description = "厂商: deepseek/qwen/glm/kimi/ollama/other")
    private String provider;

    @Schema(title = "model", description = "模型标识, 如 deepseek-chat")
    private String model;

    @Schema(title = "baseUrl", description = "接口地址")
    private String baseUrl;

    @Schema(title = "hasApiKey", description = "是否已配置API密钥")
    private Boolean hasApiKey;

    @Schema(title = "isDefault", description = "是否默认模型(0否,1是)")
    private Integer isDefault;

    @Schema(title = "enabled", description = "是否启用(0禁用,1启用)")
    private Integer enabled;

    @Schema(title = "sort", description = "排序")
    private Integer sort;

    @Schema(title = "remark", description = "备注")
    private String remark;

    @Schema(title = "createTime", description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}