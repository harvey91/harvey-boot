package com.harvey.screen.model.query;

import com.harvey.common.model.query.Query;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 信发设备分组分页查询
 *
 * @author Harvey
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ScreenDeviceGroupQuery extends Query {

    /** 分组名称关键字 */
    private String keywords;
}