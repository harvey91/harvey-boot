package com.harvey.system.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Harvey
 * @date 2024-11-06 21:50
 **/
@Data
@Builder
public class LoginUser implements UserDetails {

    private Long userId;

    private String username;

    private String password;

    private Long deptId;

    /** 用户唯一标识 */
    private String uuid;

    /** 登录时间 */
    private Long loginTime;

    /** 过期时间 */
    private Long expireTime;

    /** * 登录IP地址 */
    private String ipAddress;

    /** * 登录地点 */
    private String loginLocation;

    /** 浏览器类型 */
    private String browser;

    /** 操作系统 */
    private String os;

    /** 数据权限列表 */
    private List<Long> dataScopes;

    /** 菜单权限列表 */
    private List<String> permissions;

    private Boolean enabled;

    private Collection<SimpleGrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}
