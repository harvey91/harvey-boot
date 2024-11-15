package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统用户角色关联表
 * </p>
 *
 * @author harvey
 * @since 2024-11-07
 */
@Data
@Builder
@TableName("sys_user_role")
@Schema(title = "SysUserRole对象", description = "系统用户角色关联表")
public class SysUserRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "id", description = "主键id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(title = "userId", description = "用户id")
    private Long userId;

    @Schema(title = "roleId", description = "角色id")
    private Long roleId;

    @Schema(title = "createTime", description = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
