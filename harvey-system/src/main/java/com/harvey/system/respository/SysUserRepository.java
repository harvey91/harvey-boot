package com.harvey.system.respository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.entity.SysUser;
import com.harvey.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-28 10:58
 **/
//@Repository
@RequiredArgsConstructor
public class SysUserRepository extends ServiceImpl<SysUserMapper, SysUser> {
    private final SysUserMapper sysUserMapper;

    public SysUser findByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    public List<SysUser> selectList(SysUser user) {
        QueryWrapper<SysUser> query = Wrappers.query();
        query.eq("deleted", 1);
        return sysUserMapper.selectList(query);
    }

    public int saveUser(SysUser user) {
        if (user == null) {
            throw  new RuntimeException("用户数据为空");
        }
        if (user.getId() != null) {
            return sysUserMapper.insert(user);
        }
        return sysUserMapper.updateById(user);
    }
}
