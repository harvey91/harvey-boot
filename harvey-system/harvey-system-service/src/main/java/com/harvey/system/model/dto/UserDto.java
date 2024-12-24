package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Schema(title = "id", description = "用户id")
    private Long id;

    @Schema(title = "username", description = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 4, max = 18, message = "用户名长度必须是4-18个字符")
    private String username;

    @Schema(title = "password", description = "密码")
    private String password;

    @NotBlank(message = "昵称不能为空")
    @Schema(title = "nickname", description = "昵称")
    private String nickname;

    @Schema(title = "avatar", description = "头像")
    private String avatar;

    @Schema(title = "gender", description = "性别")
    private Integer gender;

    @Schema(title = "phone", description = "手机号")
    private String phone;

    @Schema(title = "email", description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;

    @Schema(title = "deptId", description = "部门id")
    private Long deptId;

    @Schema(title = "enabled", description = "是否启用：0禁用，1启用")
    private Integer enabled;

    @Schema(title = "roleIds", description = "角色id集合")
    private Set<Long> roleIds;

    @Schema(title = "postIds", description = "职位id集合")
    private Set<Long> postIds;
}
