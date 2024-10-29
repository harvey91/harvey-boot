package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.entity.SysUser;

/**
 * @author Harvey
 * @date 2024-10-24 21:37
 **/
public interface SysUserService {

    SysUser findById(Long id);

    SysUser findByUsername(String username);

    Page<SysUser> selectUserList(Page<SysUser> page);

    int saveUser(SysUser user);

}
