package com.harvey.ai.mapstruct;

import com.harvey.ai.model.entity.AiDocChunk;
import com.harvey.ai.model.vo.AiDocChunkVO;
import com.harvey.core.mapstruct.IConverter;
import org.mapstruct.Mapper;

/**
 * AI知识库文档分块 转换类
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper(componentModel = "spring")
public interface AiDocChunkConverter extends IConverter<AiDocChunk, Object, AiDocChunkVO> {
}