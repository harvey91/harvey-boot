package com.harvey.system.model.query;

import lombok.Data;

/**
* 操作日志表 Query类
*
* @author harvey
* @since 2024-11-21
*/
@Data
public class LogOpQuery extends Query {
    /* Query属性需要根据查询条件自定义 */

    private Integer result;
}
