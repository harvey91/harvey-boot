package com.harvey.screen.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 信发设备分组表单
 *
 * @author Harvey
 */
@Data
@Schema(title = "ScreenDeviceGroupDto")
public class ScreenDeviceGroupDto {

    private Long id;

    @NotBlank(message = "分组名称不能为空")
    @Schema(title = "分组名称")
    private String groupName;

    @Schema(title = "描述")
    private String remark;

    @Schema(title = "排序")
    private Integer sort;

    @Schema(title = "是否启用(0禁用 1启用)")
    private Integer enabled;
}