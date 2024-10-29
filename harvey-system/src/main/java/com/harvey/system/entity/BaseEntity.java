package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Harvey
 * @date 2024-10-28 21:28
 **/
@Data
public class BaseEntity implements Serializable {

    @ApiModelProperty("创建时间")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime updateTime;

    @ApiModelProperty("逻辑删除")
    @JsonIgnore
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    public Integer deleted;

}
