package com.harvey.ai.mapstruct;

import com.harvey.ai.model.entity.AiDoc;
import com.harvey.ai.model.vo.AiDocVO;
import com.harvey.core.mapstruct.IConverter;
import org.mapstruct.Mapper;

/**
 * AI知识库文档 转换类
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper(componentModel = "spring")
public interface AiDocConverter extends IConverter<AiDoc, Object, AiDocVO> {
}