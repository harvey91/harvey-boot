package com.harvey.system.model.query;

import lombok.Data;

/**
* 系统通知指定用户表 查询类
*
* @author harvey
* @since 2024-11-20
*/
@Data
public class NoticeUserQuery extends Query {

    private Long userId;

    private Integer isRead;
}
