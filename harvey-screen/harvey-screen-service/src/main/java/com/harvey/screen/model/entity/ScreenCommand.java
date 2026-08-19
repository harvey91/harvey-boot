package com.harvey.screen.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发指令记录
 *
 * @author Harvey
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("screen_command")
@Schema(title = "ScreenCommand", description = "信发指令记录表")
public class ScreenCommand extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "设备id")
    private Long deviceId;

    @Schema(title = "设备编号")
    private String deviceNo;

    @Schema(title = "指令编码")
    private Integer cmdCode;

    @Schema(title = "指令名称")
    private String cmdName;

    @Schema(title = "指令参数(JSON)")
    private String params;

    @Schema(title = "状态(0待发送 1已发送 2已执行 3失败 4超时)")
    private Integer status;

    @Schema(title = "报文序号(应答关联)")
    private Long seq;

    @Schema(title = "发送时间")
    private LocalDateTime sendTime;

    @Schema(title = "应答时间")
    private LocalDateTime ackTime;

    @Schema(title = "应答说明")
    private String ackMessage;
}
