package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 信发分组批量下发
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenGroupSendDto")
public class ScreenGroupSendDto {

    @NotNull(message = "分组id不能为空")
    @Schema(title = "分组id")
    private Long groupId;

    @NotBlank(message = "下发类型不能为空")
    @Schema(title = "下发类型：command-指令 marquee-滚动字幕 media-媒体推送")
    private String sendType;

    @Schema(title = "指令编码(sendType=command时必填)")
    private Integer cmdCode;

    @Schema(title = "指令参数(sendType=command时可选)")
    private Map<String, Object> params;

    @Schema(title = "字幕模板id(sendType=marquee时必填)")
    private Long marqueeId;

    @Schema(title = "媒体id(sendType=media时必填)")
    private Long mediaId;
}