package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 信发媒体推送
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenMediaPushDto")
public class ScreenMediaPushDto {

    @NotNull(message = "媒体id不能为空")
    @Schema(title = "媒体id")
    private Long mediaId;

    @NotBlank(message = "设备编号不能为空")
    @Schema(title = "设备编号")
    private String deviceNo;
}