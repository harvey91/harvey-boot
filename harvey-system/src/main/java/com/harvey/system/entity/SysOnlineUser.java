package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 系统在线用户表
 * </p>
 *
 * @author harvey
 * @since 2024-11-14
 */
@Data
@TableName("sys_online_user")
@Schema(title = "SysOnlineUser对象",description = "系统在线用户表")
public class SysOnlineUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(title = "uuid",description = "主键id")
    @TableId("uuid")
    private String uuid;

    @Schema(title = "userId",description = "用户id")
    private Long userId;

    @Schema(title = "username",description = "用户名")
    private String username;

    @Schema(title = "deptName",description = "所属部门")
    private String deptName;

    @Schema(title = "ip",description = "登录IP")
    private String ip;

    @Schema(title = "location",description = "登录地点")
    private String location;

    @Schema(title = "browser",description = "浏览器")
    private String browser;

    @Schema(title = "os",description = "操作系统")
    private String os;

    @Schema(title = "createTime",description = "创建时间")
    private LocalDateTime createTime;

    @Schema(title = "expireTime",description = "过期时间")
    private LocalDateTime expireTime;

    @Schema(title = "status",description = "在线状态")
    private Integer status;

}
