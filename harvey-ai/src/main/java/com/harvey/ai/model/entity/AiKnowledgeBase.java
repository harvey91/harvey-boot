package com.harvey.ai.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * AI知识库表
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_knowledge_base")
@Schema(title = "AiKnowledgeBase对象", description = "AI知识库表")
public class AiKnowledgeBase extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "name", description = "知识库名称")
    private String name;

    @Schema(title = "description", description = "知识库描述")
    private String description;

    @Schema(title = "docCount", description = "文档数量")
    private Integer docCount;

    @Schema(title = "chunkCount", description = "分块数量")
    private Integer chunkCount;

}