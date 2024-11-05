package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 系统部门表
 * </p>
 *
 * @author harvey
 * @since 2024-10-29
 */
@Data
@TableName("sys_dept")
@ApiModel(value = "SysDept对象", description = "系统部门表")
public class SysDept extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，部门id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("部门编号")
    private String deptCode;

    @ApiModelProperty("部门描述")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否启用：0禁用，1启用")
    private Integer enabled;
}
