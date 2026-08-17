package com.harvey.ai.model.query;

import com.harvey.common.model.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author harvey
 * @since 2026-08-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AiDocQuery extends Query {

    private Long kbId;
}