package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * @author Harvey
 * @date 2024-10-24 21:20
 **/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "sys_user")
@Schema(title = "SysUser对象", description = "系统用户表")
public class SysUser extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "username", description = "用户名")
    public String username;

    @JsonIgnore
    @Schema(title = "password", description = "密码")
    public String password;

    @Schema(title = "nickname", description = "昵称")
    public String nickname;

    @Schema(title = "avatar", description = "头像")
    public String avatar;

    @Schema(title = "gender", description = "性别")
    public Integer gender;

    @Schema(title = "phone", description = "手机号")
    public String phone;

    @Schema(title = "email", description = "邮箱")
    public String email;

    @Schema(title = "deptId", description = "部门id")
    public Long deptId;

}
