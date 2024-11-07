package com.harvey.system.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Harvey
 * @date 2024-11-07 10:34
 **/
@Data
public class UserDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    public Long id;

    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    public String username;

    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty("昵称")
    public String nickname;

    @ApiModelProperty("头像")
    public String avatar;

    @ApiModelProperty("性别")
    public Integer gender;

    @ApiModelProperty("手机号")
    public String phone;

    @ApiModelProperty("邮箱")
    public String email;

    @ApiModelProperty("部门id")
    public Long deptId;

    @ApiModelProperty("是否启用：0禁用，1启用")
    public Integer enabled;

    @ApiModelProperty("角色id集合")
    public Set<Long> roleIds;

    @ApiModelProperty("职位id集合")
    public Set<Long> postIds;
}
