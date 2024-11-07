package com.harvey.system.security;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
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

    private Set<Long> dataScopes;

    private Set<String> permissions;

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
