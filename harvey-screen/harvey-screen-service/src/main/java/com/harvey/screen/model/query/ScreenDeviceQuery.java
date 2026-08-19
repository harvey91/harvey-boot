package com.harvey.screen.model.query;

import com.harvey.common.model.query.Query;
import lombok.Data;

/**
 * 信发设备分页查询
 *
 * @author Harvey
 */
@Data
public class ScreenDeviceQuery extends Query {

    /** 在线状态(0离线 1在线)，不传查全部 */
    private Integer status;
}
