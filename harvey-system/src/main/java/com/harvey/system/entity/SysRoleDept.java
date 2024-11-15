package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统部门角色关联表
 * </p>
 *
 * @author harvey
 * @since 2024-11-07
 */
@Data
@TableName("sys_role_dept")
@Schema(title = "SysRoleDept对象", description = "系统部门角色关联表")
public class SysRoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "deptId", description = "部门id")
    private Long deptId;

    @Schema(title = "roleId", description = "角色id")
    private Long roleId;

    @Schema(title = "createTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
