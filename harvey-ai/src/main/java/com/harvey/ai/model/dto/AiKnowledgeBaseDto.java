package com.harvey.ai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiKnowledgeBaseDto")
public class AiKnowledgeBaseDto {

    private Long id;

    @NotBlank(message = "知识库名称不能为空")
    @Schema(title = "name", description = "知识库名称")
    private String name;

    @Schema(title = "description", description = "知识库描述")
    private String description;

    @Schema(title = "enabled", description = "是否启用(0禁用,1启用)", defaultValue = "1")
    private Integer enabled;

    @Schema(title = "sort", description = "排序")
    private Integer sort;

    @Schema(title = "remark", description = "备注")
    private String remark;
}