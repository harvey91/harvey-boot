package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.enums.ErrorCodeEnum;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.mapstruct.UserConverter;
import com.harvey.system.model.dto.PasswordDto;
import com.harvey.system.model.dto.UserDto;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.query.UserQuery;
import com.harvey.system.model.vo.OptionVO;
import com.harvey.system.model.vo.UserInfoVO;
import com.harvey.system.model.vo.UserVO;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.service.UserService;
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

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<User> formById(@PathVariable(value = "id") Long id) {
        User user = userService.getById(id);
        return RespResult.success(user);
    }

    @Operation(summary = "个人信息")
    @GetMapping("/me")
    public RespResult<UserInfoVO> me() {
        LoginUserVO loginUserVO = SecurityUtil.getLoginUserVO();
        if (ObjectUtils.isEmpty(loginUserVO)) {
            throw new BusinessException(ErrorCodeEnum.NOT_LOGIN);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setUserId(loginUserVO.getUserId());
        userInfoVO.setUsername(loginUserVO.getUsername());
        userInfoVO.setNickname(loginUserVO.getNickname());
        userInfoVO.setAvatar(loginUserVO.getAvatar());
        // TODO 角色列表
//        userInfoVO.setRoles(loginUserVO.getAuthorities());
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
    public RespResult<UserVO> modifyProfile() {

        return RespResult.success();
    }

    @Operation(summary = "修改个人密码")
    @PutMapping("/password")
    public RespResult<UserVO> password() {

        return RespResult.success();
    }

    @Operation(summary = "绑定手机号")
    @PutMapping("/phone")
    public RespResult<UserVO> phone() {

        return RespResult.success();
    }

    @Operation(summary = "绑定邮箱号")
    @PutMapping("/email")
    public RespResult<UserVO> email() {

        return RespResult.success();
    }

    @Operation(summary = "用户下拉列表")
    @GetMapping("/options")
    public RespResult<List<OptionVO>> options() {
        return RespResult.success(userService.userOptionList());
    }

    @Operation(summary = "发送手机/邮箱验证码")
    @GetMapping("/send-verification-code")
    public RespResult<UserVO> sendCode() {

        return RespResult.success();
    }
}
