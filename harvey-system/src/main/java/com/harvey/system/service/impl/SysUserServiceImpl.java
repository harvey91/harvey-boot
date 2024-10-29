package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.entity.SysUser;
import com.harvey.system.mapper.SysUserMapper;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Harvey
 * @date 2024-10-24 21:38
 **/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private final SysUserMapper sysUserMapper;

    @Override
    public SysUser findById(Long id) {
        return getById(id);
    }

    @Override
    public SysUser findByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public Page<SysUser> selectUserList(Page<SysUser> page) {
        QueryWrapper<SysUser> query = Wrappers.query();
        query.orderByDesc("create_time");
        return sysUserMapper.selectPage(page, query);
    }

    @Override
    public int saveUser(SysUser user) {
        if (user == null) {
            throw  new RuntimeException("用户数据为空");
        }
        if (user.getId() == null) {
            return sysUserMapper.insert(user);
        }
        return sysUserMapper.updateById(user);
    }

}
