package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 信发设备表单
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenDeviceDto")
public class ScreenDeviceDto {

    private Long id;

    @NotBlank(message = "设备编号不能为空")
    @Schema(title = "设备编号")
    private String deviceNo;

    @NotBlank(message = "设备名称不能为空")
    @Schema(title = "设备名称")
    private String deviceName;

    @Schema(title = "设备型号")
    private String model;

    @Schema(title = "登录令牌")
    private String secret;

    @Schema(title = "描述")
    private String remark;

    @Schema(title = "是否启用(0禁用 1启用)")
    private Integer enabled;
}
