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
@Schema(title = "AiKnowledgeBaseVO")
public class AiKnowledgeBaseVO {
    private Long id;

    @Schema(title = "name", description = "知识库名称")
    private String name;

    @Schema(title = "description", description = "知识库描述")
    private String description;

    @Schema(title = "docCount", description = "文档数量")
    private Integer docCount;

    @Schema(title = "chunkCount", description = "分块数量")
    private Integer chunkCount;

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