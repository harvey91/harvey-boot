package com.harvey.system.model.cache;

import com.harvey.common.model.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 缓存 key 分页查询参数
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CacheKeyQuery extends Query {

    /** 匹配模式，如 login:*、*token*，为空表示全部 */
    private String pattern;
}
