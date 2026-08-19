package com.harvey.ai.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.ai.model.entity.AiConversation;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * AI客服会话记录表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2026-08-18
 */
@Mapper
public interface AiConversationMapper extends BaseMapper<AiConversation> {

}