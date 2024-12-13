package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author harvey
 * @since 2024-10-29
 */
@Data
@Schema(title = "DeptDto")
public class DeptDto {

    @NotNull(message = "上级id不能为空")
    @Schema(title = "parentId",description = "父级id")
    private Long parentId;

    @NotBlank(message = "部门名称不能为空")
    @Schema(title = "deptName",description = "部门名称")
    private String deptName;

    @NotBlank(message = "部门编号不能为空")
    @Schema(title = "deptCode",description = "部门编号")
    private String deptCode;
}
