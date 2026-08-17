package com.harvey.ai.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@Schema(title = "AiDocChunkVO")
public class AiDocChunkVO {
    private Long id;

    @Schema(title = "docId", description = "文档ID")
    private Long docId;

    @Schema(title = "chunkIndex", description = "分块序号(从0开始)")
    private Integer chunkIndex;

    @Schema(title = "content", description = "分块内容")
    private String content;

    @Schema(title = "charCount", description = "字符数")
    private Integer charCount;
}