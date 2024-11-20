package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-11-20 17:27
 **/
@Data
public class NoticeDto {

    @Schema(description = "通知ID")
    private Long id;

    @NotBlank(message = "通知标题不能为空")
    @Schema(description = "通知标题")
    private String title;

    @NotBlank(message = "通知内容不能为空")
    @Schema(description = "通知内容")
    private String content;

    @NotNull(message = "通知类型不能为空")
    @Schema(description = "通知类型")
    private Integer type;

    @Schema(description = "通知状态(0未发布，1已发布，2已撤回)")
    private Integer status;

    @NotBlank(message = "通知等级不能为空")
    @Schema(description = "通知等级")
    private String level;

    @Schema(description = "通知目标类型(1全体，2指定人)")
    private Integer targetType;

    @Schema(description = "通知指定人ID集合")
    private List<Long> targetUserIds;
}
