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
 * 系统职位表
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_post")
@ApiModel(value = "SysPost对象", description = "系统职位表")
public class SysPost extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，职位id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("职位名称")
    private String postName;

    @ApiModelProperty("职级")
    private Integer postLevel;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

}
