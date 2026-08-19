package com.harvey.screen.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 设备上报消息体(状态变化/事件/截屏回传等)
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportMessage {

    /** 上报类型(自定义，如 1=状态 2=事件 3=截屏) */
    private int reportType;

    /** 设备编号 */
    private String deviceNo;

    /** 客户端时间戳(ms) */
    private long timestamp;

    /** 上报数据 */
    private Map<String, Object> data;
}