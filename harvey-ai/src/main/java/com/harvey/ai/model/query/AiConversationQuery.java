package com.harvey.ai.model.query;

import com.harvey.common.model.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author harvey
 * @since 2026-08-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiConversationQuery extends Query {

    /** 会话状态(0进行中,1已结束) */
    private Integer status;
}