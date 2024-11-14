package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 系统配置表
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@TableName("sys_config")
@ApiModel(value = "SysConfig对象", description = "系统配置表")
public class SysConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，职位id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("配置名称")
    private String configName;

    @ApiModelProperty("配置键key")
    private String configKey;

    @ApiModelProperty("配置值value")
    private String configValue;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("是否启用：0禁用，1启用")
    private Integer enabled;

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
