package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
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
@EqualsAndHashCode(callSuper = true)
@TableName("sys_role")
@Schema(title = "Role对象", description = "系统角色表")
public class Role extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "roleCode", description = "角色编码")
    private String roleCode;

    @Schema(title = "roleName", description = "角色名称")
    private String roleName;

    @Schema(title = "dataScope", description = "数据权限(0全部数据，1部门及子部门数据，2本部门数据，3本人数据)")
    private Integer dataScope;

}
