package com.harvey.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.ai.model.entity.AiFaq;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * AI客服常见问题表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Mapper
public interface AiFaqMapper extends BaseMapper<AiFaq> {

}