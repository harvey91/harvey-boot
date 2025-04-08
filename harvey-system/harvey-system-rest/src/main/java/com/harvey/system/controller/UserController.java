package com.harvey.system.controller;

import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.constant.Constant;
import com.harvey.system.model.dto.*;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import com.harvey.common.enums.ContactTypeEnum;
import com.harvey.common.enums.ErrorCodeEnum;
import com.harvey.common.enums.PlatformEnum;
import com.harvey.common.enums.VerifyTypeEnum;
import com.harvey.common.exception.BadParameterException;
import com.harvey.common.exception.BusinessException;
import com.harvey.system.mapstruct.UserConverter;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.query.UserQuery;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.UserInfoVO;
import com.harvey.system.model.vo.UserVO;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.security.service.OnlineUserCacheService;
import com.harvey.system.service.UserService;
import com.harvey.system.service.VerifyCodeService;
import com.harvey.common.utils.AssertUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户表 前端控制器
 *
 * @author Harvey
 * @date 2024-10-24 21:40
 **/
@Tag(name = "用户管理")
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserConverter userConverter;
    private final VerifyCodeService verifyCodeService;
    private final OnlineUserCacheService onlineUserCacheService;
    private final PasswordEncoder passwordEncoder;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<User> formById(@PathVariable(value = "id") Long id) {
        User user = userService.getById(id);
        return RespResult.success(user);
    }

    @Operation(summary = "个人信息")
    @GetMapping("/me")
    public RespResult<UserInfoVO> me() {
        if (SecurityUtil.getLoginUserVO().isEmpty()) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN);
        }
        LoginUserVO loginUserVO = SecurityUtil.getLoginUserVO().get();
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserId(loginUserVO.getUserId());
        userInfoVO.setUsername(loginUserVO.getUsername());
        userInfoVO.setNickname(loginUserVO.getNickname());
        userInfoVO.setAvatar(loginUserVO.getAvatar());
        List<String> roleCodeList = loginUserVO.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        userInfoVO.setRoles(roleCodeList);
        userInfoVO.setPerms(loginUserVO.getPermissions());
        return RespResult.success(userInfoVO);
    }

    @Operation(summary = "用户分页列表")
    @PreAuthorize("@ex.hasPerm('sys:user:list')")
    @GetMapping("/page")
    public RespResult<PageResult<UserVO>> page(UserQuery query) {
        Page<UserVO> userPage = userService.selectUserPage(query);
        return RespResult.success(PageResult.of(userPage));
    }

    @Operation(summary = "新增用户")
    @PreAuthorize("@ex.hasPerm('sys:user:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated UserDto userDto) {
        if (ObjectUtils.isEmpty(userDto.getId())) {
            if (ObjectUtils.isEmpty(userDto.getPassword())) {
                // 设置默认密码
                userDto.setPassword(Constant.DEFAULT_PASSWORD);
            }
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userService.createUser(userDto);
        } else {
            userService.modifyUser(userDto);
        }
        return RespResult.success();
    }

    @Operation(summary = "编辑用户")
    @PreAuthorize("@ex.hasPerm('sys:user:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated UserDto userDto) {
        userService.modifyUser(userDto);
        return RespResult.success();
    }

    @Operation(summary = "重置密码")
    @PreAuthorize("@ex.hasPerm('sys:user:password:rest')")
    @PutMapping("/password/reset")
    public RespResult<String> resetPassword(@RequestBody @Validated PasswordDto passwordDto) {
        User user = new User();
        user.setId(passwordDto.getId());
        user.setPassword(passwordEncoder.encode(passwordDto.getPassword()));
        userService.updateById(user);
        return RespResult.success();
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("ex.hasPerm('sys:user:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("用户id不能为空");
        }
        userService.removeByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "获取个人信息")
    @GetMapping("/profile")
    public RespResult<UserVO> getProfile() {
        Long userId = SecurityUtil.getUserId();
        User user = userService.getById(userId);
        UserVO userVO = userConverter.toVO(user);
        return RespResult.success(userVO);
    }

    @Operation(summary = "获取个人信息")
    @PutMapping("/profile")
    public RespResult<UserVO> modifyProfile(@RequestBody ProfileDto profileDto) {
        profileDto.setId(SecurityUtil.getUserId());
        userService.modifyProfile(profileDto);
        return RespResult.success();
    }

    @Operation(summary = "修改个人密码")
    @PutMapping("/password")
    public RespResult<UserVO> password(@RequestBody @Validated ModifyPasswordDto dto) {
        Long userId = SecurityUtil.getUserId();
        AssertUtil.isTrue(!dto.getNewPassword().equals(dto.getConfirmPassword()), "两次密码不一致");

        User user = userService.getById(userId);
        AssertUtil.isTrue(ObjectUtils.isEmpty(user), "用户不存在");
        AssertUtil.isTrue(!passwordEncoder.matches(dto.getOldPassword(), user.getPassword()), "原密码不正确");
        AssertUtil.isTrue(passwordEncoder.matches(dto.getNewPassword(), user.getPassword()), "新密码不能与原密码相同");

        User entity = new User();
        entity.setId(userId);
        entity.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userService.updateById(entity);
        // 改完密码强制下线
        onlineUserCacheService.delete(SecurityUtil.getUuid());
        return RespResult.success();
    }

    @Operation(summary = "绑定手机号")
    @PutMapping("/phone")
    public RespResult<UserVO> phone(@RequestBody @Validated PhoneDto phoneDto) {
        phoneDto.setUserId(SecurityUtil.getUserId());
        userService.bindPhone(phoneDto);
        return RespResult.success();
    }

    @Operation(summary = "绑定邮箱号")
    @PutMapping("/email")
    public RespResult<UserVO> email(@RequestBody @Validated EmailDto emailDto) {
        emailDto.setUserId(SecurityUtil.getUserId());
        userService.bindEmail(emailDto);
        return RespResult.success();
    }

    @Operation(summary = "用户下拉列表")
    @GetMapping("/options")
    public RespResult<List<OptionVO<Long>>> options() {
        return RespResult.success(userService.userOptionList());
    }

    @Operation(summary = "发送手机/邮箱验证码")
    @GetMapping("/send-verification-code")
    public RespResult<UserVO> sendCode(@RequestParam("contact") String contact,
                                       @RequestParam("contactType") String contactTypeStr) {
        Long userId = SecurityUtil.getUserId();
        User user = userService.getById(userId);
        int contactType = 0;
        if (contactTypeStr.equals(ContactTypeEnum.PHONE.name())) {
            AssertUtil.isTrue(!PhoneUtil.isPhone(contact), "手机号格式不正确");
            AssertUtil.isTrue(contact.equals(user.getPhone()), "手机号已绑定，无需再绑定");
            contactType = ContactTypeEnum.PHONE.getValue();
        } else if (contactTypeStr.equals(ContactTypeEnum.EMAIL.name())) {
            AssertUtil.isTrue(contact.equals(user.getEmail()), "邮箱已绑定，无需再绑定");
            contactType = ContactTypeEnum.EMAIL.getValue();
        } else {
            throw new BadParameterException("未知的验证类型");
        }

        verifyCodeService.sendCode(userId, contact, contactType,
                VerifyTypeEnum.BIND.getValue(), PlatformEnum.SYSTEM.getValue());
        return RespResult.success();
    }
}
