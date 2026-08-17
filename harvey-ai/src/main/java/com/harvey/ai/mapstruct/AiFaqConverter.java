package com.harvey.ai.mapstruct;

import com.harvey.ai.model.dto.AiFaqDto;
import com.harvey.ai.model.entity.AiFaq;
import com.harvey.ai.model.vo.AiFaqVO;
import com.harvey.core.mapstruct.IConverter;
import org.mapstruct.Mapper;

/**
 * AI客服常见问题 转换类
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper(componentModel = "spring")
public interface AiFaqConverter extends IConverter<AiFaq, AiFaqDto, AiFaqVO> {
}