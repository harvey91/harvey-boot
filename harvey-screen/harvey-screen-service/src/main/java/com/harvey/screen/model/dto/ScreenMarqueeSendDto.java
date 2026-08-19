package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 信发滚动字幕下发
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenMarqueeSendDto")
public class ScreenMarqueeSendDto {

    @NotNull(message = "模板id不能为空")
    @Schema(title = "模板id")
    private Long marqueeId;

    @NotBlank(message = "设备编号不能为空")
    @Schema(title = "设备编号")
    private String deviceNo;
}