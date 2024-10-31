package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-10-24 21:20
 **/
@Data
@TableName(value = "sys_user")
@ApiModel(value = "SysUser对象", description = "系统用户表")
public class SysUser extends BaseEntity {

    @ApiModelProperty("主键，用户id")
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;

    @ApiModelProperty("用户名")
    public String username;

    @JsonIgnore
    @ApiModelProperty("密码")
    public String password;

    @ApiModelProperty("昵称")
    public String nickname;

    @ApiModelProperty("头像")
    public String avatar;

    @ApiModelProperty("手机号")
    public String phone;

    @ApiModelProperty("邮箱")
    public String email;

    @ApiModelProperty("部门id")
    public Long deptId;

    @ApiModelProperty("是否启用：0禁用，1启用")
    public Integer enabled;
}
