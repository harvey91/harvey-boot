package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.enums.VerifyTypeEnum;
import com.harvey.common.utils.AssertUtil;
import com.harvey.system.mapper.UserMapper;
import com.harvey.system.mapstruct.UserConverter;
import com.harvey.system.model.dto.EmailDto;
import com.harvey.system.model.dto.PhoneDto;
import com.harvey.system.model.dto.ProfileDto;
import com.harvey.system.model.dto.UserDto;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.entity.UserRole;
import com.harvey.system.model.query.UserQuery;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.UserVO;
import lombok.RequiredArgsConstructor;
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
    private final UserMapper mapper;
    private final UserConverter converter;
    private final RoleService roleService;
    private final UserRoleService userRoleService;
    private final VerifyCodeService verifyCodeService;

    public User findByUsername(String username) {
        return mapper.selectByUsername(username);
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
        Page<User> userPage = mapper.selectPage(page, queryWrapper);
        return converter.toPage(userPage);
    }

    /**
     * 新增用户
     *
     * @param userDto
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void createUser(UserDto userDto) {
        long count = mapper.countByUsername(userDto.getUsername());
        AssertUtil.isTrue(count > 0, "用户名已存在");

        User entity = converter.toEntity(userDto);
        mapper.insert(entity);

        // 保存用户角色
        Set<Long> roleIds = userDto.getRoleIds();
        if (!ObjectUtils.isEmpty(roleIds)) {
            userRoleService.create(entity.getId(), roleIds);
        }
    }

    /**
     * 修改用户信息
     *
     * @param userDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyUser(UserDto userDto) {
        User user = this.getById(userDto.getId());
        AssertUtil.isEmpty(user, "找不到用户");
        AssertUtil.isEmpty(user.getId(), "找不到用户");

        if (!userDto.getUsername().equals(user.getUsername())) {
            // 当用户名有变化，需要检测新用户名是否存在
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                    .eq(User::getUsername, userDto.getUsername())
                    .ne(User::getId, userDto.getId());
            long count = mapper.selectCount(queryWrapper);
            AssertUtil.isTrue(count > 0, "用户名已存在");
        }
        // 修改用户
        User entity = converter.toEntity(userDto);
        this.updateById(entity);

        Set<Long> roleIds = userDto.getRoleIds();
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, user.getId());
        // 先删除原先的用户角色
        userRoleService.remove(queryWrapper);
        if (!ObjectUtils.isEmpty(roleIds)) {
            // 保存新的用户角色
            userRoleService.create(user.getId(), roleIds);
        }
    }

    /**
     * 更新个人信息
     * @param profileDto
     */
    @Transactional(rollbackFor = Exception.class)
    public void modifyProfile(ProfileDto profileDto) {
        User entity = new User();
        entity.setId(profileDto.getId());
        entity.setNickname(profileDto.getNickname());
        entity.setGender(profileDto.getGender());
        entity.setAvatar(profileDto.getAvatar());
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindPhone(PhoneDto phoneDto) {
        verifyCodeService.verify(phoneDto.getPhone(), phoneDto.getCode(), VerifyTypeEnum.BIND.getValue());
        User entity = new User();
        entity.setId(phoneDto.getUserId());
        entity.setPhone(phoneDto.getPhone());
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindEmail(EmailDto emailDto) {
        verifyCodeService.verify(emailDto.getEmail(), emailDto.getCode(), VerifyTypeEnum.BIND.getValue());
        User entity = new User();
        entity.setId(emailDto.getUserId());
        entity.setEmail(emailDto.getEmail());
        this.updateById(entity);
    }

    /**
     * 用户下拉列表
     *
     * @return
     */
    public List<OptionVO<Long>> userOptionList() {
        List<User> list = this.list();
        if (ObjectUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(item ->
                OptionVO.<Long>builder().value(item.getId()).label(item.getNickname()).build()).toList();
    }

    public String findNickname(Long id) {
        return mapper.selectNickname(id);
    }
}
