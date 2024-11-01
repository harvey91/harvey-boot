package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.system.domain.query.UserQueryParam;
import com.harvey.system.entity.SysUser;

/**
 * @author Harvey
 * @date 2024-10-24 21:37
 **/
public interface SysUserService extends IService<SysUser> {

    SysUser findById(Long id);

    SysUser findByUsername(String username);

    Page<SysUser> selectUserPage(UserQueryParam queryParam);

    int saveUser(SysUser user);

}
