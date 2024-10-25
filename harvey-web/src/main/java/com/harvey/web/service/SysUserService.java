package com.harvey.web.service;

import com.harvey.web.entity.SysUser;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-24 21:37
 **/
public interface SysUserService {

    SysUser findByUsername(String username);

    List<SysUser> selectUserList(SysUser user);
}
