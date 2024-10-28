package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Harvey
 * @date 2024-10-28 21:28
 **/
@Data
public class BaseEntity implements Serializable {

    @TableField(fill = FieldFill.INSERT)
    public Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    public Date updateTime;

    // 逻辑删除
    @JsonIgnore
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    public Integer deleted;
}
