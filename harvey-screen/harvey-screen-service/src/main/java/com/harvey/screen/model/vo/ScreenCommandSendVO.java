package com.harvey.screen.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 指令下发结果
 *
 * @author Harvey
 */
@Data
@AllArgsConstructor
@Schema(title = "ScreenCommandSendVO")
public class ScreenCommandSendVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "指令记录id")
    private Long commandId;

    @Schema(title = "报文序号")
    private Long seq;

    @Schema(title = "状态(0待发送 1已发送 2已执行 3失败)")
    private Integer status;

    @Schema(title = "结果说明")
    private String message;
}
