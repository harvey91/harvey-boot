package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(value = "SysRoleMenu对象", description = "系统角色菜单关联表")
public class SysRoleMenu implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime updateTime;
}
