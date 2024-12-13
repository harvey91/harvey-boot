package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 系统角色表
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
@Data
@Schema(title = "RoleVO")
public class RoleVO {
    private Long id;

    @Schema(title = "roleCode", description = "角色编码")
    private String roleCode;

    @Schema(title = "roleName", description = "角色名称")
    private String roleName;

    @Schema(title = "dataScope", description = "数据权限")
    private Integer dataScope;

}
