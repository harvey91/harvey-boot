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

    /** 审核状态(0待确认 1已通过 2已拒绝)，不传查全部 */
    private Integer auditStatus;

    /** 设备分组id，不传查全部 */
    private Long groupId;

    /** 是否查询已删除列表(1=是)，走逻辑删除旁路查询 */
    private Integer deleted;
}
