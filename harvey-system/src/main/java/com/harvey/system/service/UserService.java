package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.mapper.UserMapper;
import com.harvey.system.mapstruct.UserConverter;
import com.harvey.system.model.dto.PasswordDto;
import com.harvey.system.model.dto.UserDto;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.entity.UserRole;
import com.harvey.system.model.query.UserQuery;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Harvey
 * @date 2024-10-24 21:38
 **/
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {
    private final UserMapper sysUserMapper;
    private final UserConverter userConverter;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return sysUserMapper.selectByUsername(username);
    }

    public Page<UserVO> selectUserPage(UserQuery query) {
        // TODO 根据当前用户角色dataScope查询相应的部门ids

        Page<User> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                // 部门id
                .eq(query.getDeptId() != null, User::getDeptId, query.getDeptId())
                // 用户名
                .like(StringUtils.hasLength(query.getKeywords()), User::getUsername, query.getKeywords())
                // 启用状态
                .eq(query.getEnabled() != null, User::getEnabled, query.getEnabled())
                // 创建时间范围
//                .between((queryParam.getCreateTime() != null && queryParam.getCreateTime().size() == 2),
//                        User::getCreateTime, queryParam.getCreateTime().get(0), queryParam.getCreateTime().get(1))
                .orderByDesc(User::getCreateTime);
        Page<User> userPage = sysUserMapper.selectPage(page, queryWrapper);
        return userConverter.toPage(userPage);
    }

    /**
     * 新增用户
     * @param userDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDto userDto) {
        long count = sysUserMapper.countByUsername(userDto.getUsername());
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }

        User entity = userConverter.toEntity(userDto);
        // 设置默认密码
        entity.setPassword(passwordEncoder.encode("123456"));
        sysUserMapper.insert(entity);

        // 保存用户角色
        Set<Long> roleIds = userDto.getRoleIds();
        if (!ObjectUtils.isEmpty(roleIds)) {
            userRoleService.create(entity.getId(), roleIds);
        }
    }

    /**
     * 修改用户信息
     * @param userDto
     */
    @Transactional
    public void modifyUser(UserDto userDto) {
        User sysUser = sysUserMapper.selectById(userDto.getId());
        if (ObjectUtils.isEmpty(sysUser) || ObjectUtils.isEmpty(sysUser.getId())) {
            throw new BusinessException("找不到用户");
        }
        if (!userDto.getUsername().equals(sysUser.getUsername())) {
            // 当用户名有变化，需要检测新用户名是否存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, userDto.getUsername())
                    .ne(User::getId, userDto.getId());
            long count = sysUserMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException("用户名已存在");
            }
        }
        // 修改用户
        User entity = userConverter.toEntity(userDto);
        sysUserMapper.updateById(entity);

        Set<Long> roleIds = userDto.getRoleIds();
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, sysUser.getId());
        // 先删除原先的用户角色
        userRoleService.remove(queryWrapper);
        if (!ObjectUtils.isEmpty(roleIds)) {
            // 保存新的用户角色
            userRoleService.create(sysUser.getId(), roleIds);
        }
    }

    /**
     * 重置密码
     * @param passwordDto 修改密码参数
     */
    public void resetPassword(PasswordDto passwordDto) {
        User user = new User();
        user.setId(passwordDto.getId());
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        sysUserMapper.updateById(user);
    }

    /**
     * 用户下拉列表
     * @return
     */
    public List<OptionVO> userOptionList() {
        List<User> list = this.list();
        if (ObjectUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(item -> OptionVO.builder().value(item.getId()).label(item.getNickname()).build()).toList();
    }
}
