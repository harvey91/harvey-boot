package com.harvey.system.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.system.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
@Data
@Schema(title = "RoleDto")
public class RoleDto {

    private Long id;

    @NotBlank(message = "角色编码不能为空")
    @Schema(title = "roleCode", description = "角色编码")
    private String roleCode;

    @NotBlank(message = "角色名称不能为空")
    @Schema(title = "roleName", description = "角色名称")
    private String roleName;

    @NotNull(message = "数据权限不能为空")
    @Schema(title = "dataScope", description = "数据权限")
    private Integer dataScope;

}
