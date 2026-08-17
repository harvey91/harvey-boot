package com.harvey.ai.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiFaqDto")
public class AiFaqDto {

    private Long id;

    @NotBlank(message = "问题不能为空")
    @Schema(title = "question", description = "问题")
    private String question;

    @NotBlank(message = "答案不能为空")
    @Schema(title = "answer", description = "标准答案")
    private String answer;

    @Schema(title = "category", description = "分类")
    private String category;

    @Schema(title = "enabled", description = "是否启用(0禁用,1启用)", defaultValue = "1")
    private Integer enabled;

    @Schema(title = "sort", description = "排序")
    private Integer sort;

    @Schema(title = "remark", description = "备注")
    private String remark;
}