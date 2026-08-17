package com.harvey.ai.mapstruct;

import com.harvey.ai.model.dto.AiModelConfigDto;
import com.harvey.ai.model.entity.AiModelConfig;
import com.harvey.ai.model.vo.AiModelConfigVO;
import com.harvey.core.mapstruct.IConverter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
* AI模型配置 转换类
*
* @author harvey
* @since 2026-08-17
*/
@Mapper(componentModel = "spring")
public interface AiModelConfigConverter extends IConverter<AiModelConfig, AiModelConfigDto, AiModelConfigVO> {

    @Override
    @Mapping(target = "hasApiKey", expression = "java(com.harvey.common.utils.StringUtils.isNotBlank(entity.getApiKey()))")
    AiModelConfigVO toVO(AiModelConfig entity);
}