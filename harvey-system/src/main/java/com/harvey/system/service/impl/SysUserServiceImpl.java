package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.domain.dto.PasswordDto;
import com.harvey.system.domain.dto.UserDto;
import com.harvey.system.domain.query.UserQueryParam;
import com.harvey.system.domain.vo.UserVO;
import com.harvey.system.entity.SysUser;
import com.harvey.system.entity.SysUserRole;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.mapper.SysUserMapper;
import com.harvey.system.mapstruct.UserConverter;
import com.harvey.system.service.ISysRoleService;
import com.harvey.system.service.ISysUserRoleService;
import com.harvey.system.service.SysUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Harvey
 * @date 2024-10-24 21:38
 **/
@Service
@RequiredArgsConstructor
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
    private final SysUserMapper sysUserMapper;
    private final UserConverter userConverter;
    private final ISysRoleService sysRoleService;
    private final ISysUserRoleService sysUserRoleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SysUser findById(Long id) {
        return getById(id);
    }

    @Override
    public SysUser findByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    @Override
    public Page<UserVO> selectUserPage(UserQueryParam queryParam) {
        // TODO 根据当前用户角色dataScope查询相应的部门ids

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
        Page<SysUser> sysUserPage = sysUserMapper.selectPage(page, queryWrapper);
        Page<UserVO> userVOPage = userConverter.toPage(sysUserPage);

        return userVOPage;
    }

    /**
     * 新增用户
     * @param userDto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDto userDto) {
        long count = sysUserMapper.countByUsername(userDto.getUsername());
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        SysUser entity = userConverter.toEntity(userDto);
        // 设置默认密码
        entity.setPassword(passwordEncoder.encode("123456"));
        sysUserMapper.insert(entity);

        // 保存用户角色
        Set<Long> roleIds = userDto.getRoleIds();
        if (!ObjectUtils.isEmpty(roleIds)) {
            sysUserRoleService.create(entity.getId(), roleIds);
        }
    }

    /**
     * 修改用户信息
     * @param userDto
     */
    @Override
    @Transactional
    public void modifyUser(UserDto userDto) {
        SysUser sysUser = sysUserMapper.selectById(userDto.getId());
        if (ObjectUtils.isEmpty(sysUser) || ObjectUtils.isEmpty(sysUser.getId())) {
            throw new BusinessException("找不到用户");
        }
        if (!userDto.getUsername().equals(sysUser.getUsername())) {
            // 当用户名有变化，需要检测新用户名是否存在
            LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                    .eq(SysUser::getUsername, userDto.getUsername())
                    .ne(SysUser::getId, userDto.getId());
            long count = sysUserMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException("用户名已存在");
            }
        }
        // 修改用户
        SysUser entity = userConverter.toEntity(userDto);
        sysUserMapper.updateById(entity);

        Set<Long> roleIds = userDto.getRoleIds();
        LambdaQueryWrapper<SysUserRole> queryWrapper = new LambdaQueryWrapper<SysUserRole>()
                .eq(SysUserRole::getUserId, sysUser.getId());
        // 先删除原先的用户角色
        sysUserRoleService.remove(queryWrapper);
        if (!ObjectUtils.isEmpty(roleIds)) {
            // 保存新的用户角色
            sysUserRoleService.create(sysUser.getId(), roleIds);
        }
    }

    /**
     * 重置密码
     * @param passwordDto 修改密码参数
     */
    @Override
    public void resetPassword(PasswordDto passwordDto) {
        SysUser user = new SysUser();
        user.setId(passwordDto.getId());
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        sysUserMapper.updateById(user);
    }
}
