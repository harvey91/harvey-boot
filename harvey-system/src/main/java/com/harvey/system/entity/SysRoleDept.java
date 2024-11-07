package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(value = "SysRoleDept对象", description = "系统部门角色关联表")
public class SysRoleDept implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("部门id")
    private Long deptId;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
