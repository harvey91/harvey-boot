package com.harvey.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.ai.model.entity.AiDocChunk;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * AI知识库文档分块表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper
public interface AiDocChunkMapper extends BaseMapper<AiDocChunk> {

}