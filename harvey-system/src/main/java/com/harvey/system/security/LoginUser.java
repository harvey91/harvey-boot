package com.harvey.system.security;

import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-11-06 21:50
 **/
@Data
public class LoginUser {

    private Long userId;

    private List<Integer> dataScopes;

    private List<String> permissions;
}
