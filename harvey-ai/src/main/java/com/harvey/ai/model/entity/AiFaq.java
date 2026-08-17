package com.harvey.ai.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * AI客服常见问题表
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ai_faq")
@Schema(title = "AiFaq对象", description = "AI客服常见问题表")
public class AiFaq extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "question", description = "问题")
    private String question;

    @Schema(title = "answer", description = "标准答案")
    private String answer;

    @Schema(title = "category", description = "分类")
    private String category;

    @Schema(title = "hitCount", description = "命中次数")
    private Integer hitCount;

}