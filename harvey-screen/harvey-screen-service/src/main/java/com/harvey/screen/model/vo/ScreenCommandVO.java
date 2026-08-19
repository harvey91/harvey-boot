package com.harvey.screen.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 信发指令记录视图
 *
 * @author Harvey
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "ScreenCommandVO")
public class ScreenCommandVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

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

    @Schema(title = "报文序号")
    private Long seq;

    @Schema(title = "发送时间")
    private LocalDateTime sendTime;

    @Schema(title = "应答时间")
    private LocalDateTime ackTime;

    @Schema(title = "应答说明")
    private String ackMessage;

    @Schema(title = "创建时间")
    private LocalDateTime createTime;
}
