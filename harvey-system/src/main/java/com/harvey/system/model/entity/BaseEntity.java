package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Harvey
 * @date 2024-10-28 21:28
 **/
@Data
public class BaseEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id",description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "remark", description = "描述")
    private String remark;

    @Schema(title = "sort", description = "排序", defaultValue = "99")
    private Integer sort;

    @Schema(title = "enabled", description = "是否启用(0禁用，1启用)", defaultValue = "1")
    private Integer enabled;

    @Schema(title = "createTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    public LocalDateTime createTime;

    @Schema(title = "updateTime", description = "修改时间")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    public LocalDateTime updateTime;

    @Schema(title = "deleted", description = "逻辑删除(1未删除，0已删除)", defaultValue = "1")
    @JsonIgnore
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    public Integer deleted;

}
