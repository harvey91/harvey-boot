package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.entity.SysUser;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-24 21:37
 **/
public interface SysUserService {

    SysUser findById(Long id);

    SysUser findByUsername(String username);

    Page<SysUser> selectUserList(SysUser user);

    int saveUser(SysUser user);

}
