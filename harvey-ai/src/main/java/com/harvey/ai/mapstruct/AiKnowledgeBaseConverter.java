package com.harvey.ai.mapstruct;

import com.harvey.ai.model.dto.AiKnowledgeBaseDto;
import com.harvey.ai.model.entity.AiKnowledgeBase;
import com.harvey.ai.model.vo.AiKnowledgeBaseVO;
import com.harvey.core.mapstruct.IConverter;
import org.mapstruct.Mapper;

/**
 * AI知识库 转换类
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper(componentModel = "spring")
public interface AiKnowledgeBaseConverter extends IConverter<AiKnowledgeBase, AiKnowledgeBaseDto, AiKnowledgeBaseVO> {
}