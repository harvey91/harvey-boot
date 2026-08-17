package com.harvey.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.ai.model.entity.AiModelConfig;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * AI模型配置表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper
public interface AiModelConfigMapper extends BaseMapper<AiModelConfig> {

}