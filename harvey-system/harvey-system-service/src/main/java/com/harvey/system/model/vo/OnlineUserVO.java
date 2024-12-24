package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Harvey
 * @date 2024-12-24 22:55
 **/
@Data
public class OnlineUserVO {

    @Schema(title = "uuid",description = "主键id")
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
