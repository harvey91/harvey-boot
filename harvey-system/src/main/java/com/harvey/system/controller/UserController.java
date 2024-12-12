package com.harvey.system.controller;

import cn.hutool.core.util.PhoneUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.enums.ContactTypeEnum;
import com.harvey.system.enums.ErrorCodeEnum;
import com.harvey.system.enums.PlatformEnum;
import com.harvey.system.enums.VerifyTypeEnum;
import com.harvey.system.exception.BadParameterException;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.mapstruct.UserConverter;
import com.harvey.system.model.dto.*;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.query.UserQuery;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.UserInfoVO;
import com.harvey.system.model.vo.UserVO;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.service.UserService;
import com.harvey.system.service.VerifyCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
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
        // TODO 角色列表
        userInfoVO.setRoles(List.of("ROOT"));
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
        userService.resetPassword(passwordDto);
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
        userService.modifyProfile(profileDto);
        return RespResult.success();
    }

    @Operation(summary = "修改个人密码")
    @PutMapping("/password")
    public RespResult<UserVO> password(@RequestBody @Validated ModifyPasswordDto modifyPasswordDto) {
        userService.modifyPassword(modifyPasswordDto);
        return RespResult.success();
    }

    @Operation(summary = "绑定手机号")
    @PutMapping("/phone")
    public RespResult<UserVO> phone(@RequestBody @Validated PhoneDto phoneDto) {
        userService.bindPhone(phoneDto);
        return RespResult.success();
    }

    @Operation(summary = "绑定邮箱号")
    @PutMapping("/email")
    public RespResult<UserVO> email(@RequestBody @Validated EmailDto emailDto) {
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
            if (!PhoneUtil.isPhone(contact)) {
                throw new BadParameterException("手机号格式不正确");
            }
            if (contact.equals(user.getPhone())) {
                throw new BusinessException("手机号已绑定，无需再绑定");
            }
            contactType = ContactTypeEnum.PHONE.getValue();
        } else if (contactTypeStr.equals(ContactTypeEnum.EMAIL.name())) {
            if (contact.equals(user.getEmail())) {
                throw new BusinessException("邮箱已绑定，无需再绑定");
            }
            contactType = ContactTypeEnum.EMAIL.getValue();
        } else {
            throw new BadParameterException("未知的验证类型");
        }

        verifyCodeService.sendCode(contact, contactType, VerifyTypeEnum.BIND.getValue(), PlatformEnum.SYSTEM.getValue());
        return RespResult.success();
    }
}
