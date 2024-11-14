package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel(value = "SysOnlineUser对象", description = "系统在线用户表")
public class SysOnlineUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键id")
    @TableId("uuid")
    private String uuid;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("所属部门")
    private String deptName;

    @ApiModelProperty("登录IP")
    private String ip;

    @ApiModelProperty("登录地点")
    private String location;

    @ApiModelProperty("浏览器")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("过期时间")
    private LocalDateTime expireTime;

    @ApiModelProperty("过期时间")
    private Integer status;

}
