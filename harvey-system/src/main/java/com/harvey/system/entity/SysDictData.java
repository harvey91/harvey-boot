package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 系统字典数据表
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_dict_data")
@ApiModel(value = "SysDictData对象", description = "系统字典数据表")
public class SysDictData extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，角色id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("字典编码")
    private String dictCode;

    @ApiModelProperty("字典项")
    private String label;

    @ApiModelProperty("字典值")
    private String value;

    @ApiModelProperty("前端标签tag值")
    private String tag;

    @ApiModelProperty("排序")
    private Integer sort;

}
