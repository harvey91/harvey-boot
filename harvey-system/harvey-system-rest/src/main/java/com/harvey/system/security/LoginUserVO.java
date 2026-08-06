package com.harvey.system.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-11-06 21:50
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginUserVO implements Serializable {

    private Long userId;

    private String username;

    private String nickname;

    private String avatar;

    private Long deptId;

    private Boolean isAdmin;

    /** 用户唯一标识 */
    private String uuid;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** * 登录IP地址 */
    private String ip;

    /** * 登录地点 */
    private String location;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 数据权限列表 */
    private List<Long> dataScopes;

    /** 菜单权限列表 */
    private List<String> permissions;

    /** 角色标识列表 */
    private List<String> roles;

    private Boolean enabled;

}
