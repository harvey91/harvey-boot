package com.harvey.ai.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * AI知识库文档分块表
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_doc_chunk")
@Schema(title = "AiDocChunk对象", description = "AI知识库文档分块表")
public class AiDocChunk extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "kbId", description = "知识库ID")
    private Long kbId;

    @Schema(title = "docId", description = "文档ID")
    private Long docId;

    @Schema(title = "chunkIndex", description = "分块序号(从0开始)")
    private Integer chunkIndex;

    @Schema(title = "content", description = "分块内容")
    private String content;

    @Schema(title = "charCount", description = "字符数")
    private Integer charCount;

}