package com.harvey.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 13:09
 **/
@Data
public class UserInfoVO {

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

    /**
     * 角色集合
     */
    private List<String> roles;

    /**
     * 权限集合
     */
    private List<String> perms;
}
