package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@TableName("sys_role")
@ApiModel(value = "SysRole对象", description = "系统角色表")
public class SysRole extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，角色id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("角色编码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("数据权限：0全部数据，1部门及子部门数据，2本部门数据，3本人数据")
    private Integer dataScope;

    @ApiModelProperty("角色描述")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否启用：0禁用，1启用")
    private Integer enabled;

}
