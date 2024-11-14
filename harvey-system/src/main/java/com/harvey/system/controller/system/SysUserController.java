package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.domain.dto.PasswordDto;
import com.harvey.system.domain.dto.UserDto;
import com.harvey.system.domain.query.UserQueryParam;
import com.harvey.system.domain.vo.UserInfoVO;
import com.harvey.system.domain.vo.UserVO;
import com.harvey.system.entity.SysUser;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.service.SysUserService;
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
@RestController
@RequestMapping("/system/user")
@RequiredArgsConstructor
public class SysUserController {
    private final SysUserService sysUserService;

    @GetMapping("/form/{id}")
    public RespResult<SysUser> formById(@PathVariable(value = "id") Long id) {
        SysUser sysUser = sysUserService.getById(id);
        return RespResult.success(sysUser);
    }

    @GetMapping("/me")
    public RespResult<UserInfoVO> me() {
        LoginUserVO loginUserVO = SecurityUtil.getLoginUserVO();
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

    /**
     * 用户分页列表
     * @param queryParam
     * @return
     */
    @PreAuthorize("@ex.hasPerm('sys:user:list')")
    @GetMapping("/page")
    public RespResult<PageResult<UserVO>> page(UserQueryParam queryParam) {
        Page<UserVO> userPage = sysUserService.selectUserPage(queryParam);
        return RespResult.success(PageResult.of(userPage));
    }

    /**
     * 新增用户信息
     * @param userDto
     * @return
     */
    @PreAuthorize("@ex.hasPerm('sys:user:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated UserDto userDto) {
        if (ObjectUtils.isEmpty(userDto.getId())) {
            sysUserService.createUser(userDto);
        } else {
            sysUserService.modifyUser(userDto);
        }
        return RespResult.success();
    }

    /**
     * 修改用户信息
     * @param userDto
     * @return
     */
    @PreAuthorize("@ex.hasPerm('sys:user:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated UserDto userDto) {
        sysUserService.modifyUser(userDto);
        return RespResult.success();
    }

    /**
     * 重置密码
     * @param passwordDto
     * @return
     */
    @PreAuthorize("@ex.hasPerm('sys:user:password:rest')")
    @PutMapping("/password/reset")
    public RespResult<String> resetPassword(@RequestBody @Validated PasswordDto passwordDto) {
        sysUserService.resetPassword(passwordDto);
        return RespResult.success();
    }

    /**
     * 删除用户
     * @param ids
     * @return
     */
    @PreAuthorize("ex.hasPerm('sys:user:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("用户id不能为空");
        }
        sysUserService.removeByIds(ids);
        return RespResult.success();
    }
}
