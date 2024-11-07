package com.harvey.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Harvey
 * @date 2024-11-07 17:26
 **/
@Data
public class UserVO {

    @ApiModelProperty("用户id")
    public Long id;

    @ApiModelProperty("用户名")
    public String username;

    @ApiModelProperty("昵称")
    public String nickname;

//    @ApiModelProperty("头像")
//    public String avatar;

    @ApiModelProperty("性别")
    public Integer gender;

    @ApiModelProperty("手机号")
    public String phone;

//    @ApiModelProperty("邮箱")
//    public String email;

    @ApiModelProperty("是否启用：0禁用，1启用")
    public Integer enabled;

    @ApiModelProperty("部门名称")
    public String deptName;

    @ApiModelProperty("角色名称")
    public String roleName;

    @ApiModelProperty("创建时间")
    public LocalDateTime createTime;
}
