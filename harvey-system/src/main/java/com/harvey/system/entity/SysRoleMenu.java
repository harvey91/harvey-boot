package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统角色菜单关联表
 * </p>
 *
 * @author harvey
 * @since 2024-11-06
 */
@Data
@TableName("sys_role_menu")
@Schema(title = "SysRoleMenu对象", description = "系统角色菜单关联表")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "roleId", description = "角色id")
    private Long roleId;

    @Schema(title = "menuId", description = "菜单id")
    private Long menuId;

    @Schema(title = "createTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

}
