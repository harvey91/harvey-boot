package com.harvey.screen.cluster;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 跨节点集群消息
 * <p>
 * COMMAND：请求节点在本地无会话时发布指令，设备所在节点确认本机会话后重建下发。
 * TAKEOVER：设备在新节点登录时广播，旧节点关闭残留会话，保证全局唯一会话。
 * seq 由请求节点统一生成，应答时按 deviceNo+seq 更新指令状态。
 *
 * @author Harvey
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScreenCommandForwardMessage {

    /** 消息类型：COMMAND 指令转发 / TAKEOVER 会话接管 */
    private String type;

    /** 设备编号 */
    private String deviceNo;

    /** 报文序号(请求节点生成) */
    private Long seq;

    /** 指令编码 */
    private Integer cmdCode;

    /** 指令参数 */
    private Map<String, Object> params;

    /** 来源节点id */
    private String fromNodeId;

    /** 转发时间戳(ms) */
    private Long timestamp;
}