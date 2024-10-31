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
 * 系统字典表
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@TableName("sys_dict")
@ApiModel(value = "SysDict对象", description = "系统字典表")
public class SysDict extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，角色id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("字典编码")
    private String dictCode;

    @ApiModelProperty("字典名称")
    private String dictName;

    @ApiModelProperty("角色描述")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否启用：0禁用，1启用")
    private Integer enabled;
}
