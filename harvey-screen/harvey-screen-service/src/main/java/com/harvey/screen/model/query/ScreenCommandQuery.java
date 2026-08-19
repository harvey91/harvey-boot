package com.harvey.screen.model.query;

import com.harvey.common.model.query.Query;
import lombok.Data;

/**
 * 信发指令记录分页查询
 *
 * @author Harvey
 */
@Data
public class ScreenCommandQuery extends Query {

    /** 设备编号 */
    private String deviceNo;

    /** 指令编码 */
    private Integer cmdCode;

    /** 状态(0待发送 1已发送 2已执行 3失败 4超时) */
    private Integer status;
}
