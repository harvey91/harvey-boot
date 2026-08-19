package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Map;

/**
 * 信发指令下发
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenCommandDto")
public class ScreenCommandDto {

    @NotBlank(message = "设备编号不能为空")
    @Schema(title = "设备编号")
    private String deviceNo;

    @NotNull(message = "指令编码不能为空")
    @Schema(title = "指令编码", description = "见 ScreenCommandCode")
    private Integer cmdCode;

    @Schema(title = "指令参数")
    private Map<String, Object> params;
}
