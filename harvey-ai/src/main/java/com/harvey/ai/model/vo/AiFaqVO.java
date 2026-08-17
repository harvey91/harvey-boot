package com.harvey.ai.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiFaqVO")
public class AiFaqVO {
    private Long id;

    @Schema(title = "question", description = "问题")
    private String question;

    @Schema(title = "answer", description = "标准答案")
    private String answer;

    @Schema(title = "category", description = "分类")
    private String category;

    @Schema(title = "hitCount", description = "命中次数")
    private Integer hitCount;

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