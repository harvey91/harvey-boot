package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.domain.query.UserQueryParam;
import com.harvey.system.entity.SysUser;
import com.harvey.system.mapper.SysUserMapper;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

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
    public Page<SysUser> selectUserPage(UserQueryParam queryParam) {
        Page<SysUser> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                // 部门id
                .eq(queryParam.getDeptId() != null, SysUser::getDeptId, queryParam.getDeptId())
                // 用户名
                .like(StringUtils.hasLength(queryParam.getKeywords()), SysUser::getUsername, queryParam.getKeywords())
                // 启用状态
                .eq(queryParam.getEnabled() != null, SysUser::getEnabled, queryParam.getEnabled())
                // 创建时间范围
//                .between((queryParam.getCreateTime() != null && queryParam.getCreateTime().size() == 2),
//                        SysUser::getCreateTime, queryParam.getCreateTime().get(0), queryParam.getCreateTime().get(1))
                .orderByDesc(SysUser::getCreateTime);
        return sysUserMapper.selectPage(page, queryWrapper);
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
