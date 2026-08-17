package com.harvey.ai.mapstruct;

import com.harvey.ai.model.entity.AiWorkOrder;
import com.harvey.ai.model.vo.AiWorkOrderVO;
import com.harvey.core.mapstruct.IConverter;
import org.mapstruct.Mapper;

/**
 * AI客服工单 转换类
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper(componentModel = "spring")
public interface AiWorkOrderConverter extends IConverter<AiWorkOrder, Object, AiWorkOrderVO> {
}