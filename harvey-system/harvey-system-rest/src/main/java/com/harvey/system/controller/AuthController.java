package com.harvey.system.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.IdUtil;
import com.harvey.common.result.RespResult;
import com.harvey.common.constant.CacheConstant;
import com.harvey.common.enums.LoginResultEnum;
import com.harvey.starter.redis.service.RedisService;
import com.harvey.system.model.dto.LoginDto;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.vo.CaptchaVO;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.security.service.OnlineUserCacheService;
import com.harvey.system.service.LogService;
import com.harvey.system.service.MenuService;
import com.harvey.system.service.RoleService;
import com.harvey.system.service.UserService;
import com.harvey.common.utils.StringUtils;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 用户授权
 * @author Harvey
 * @date 2024-10-30 10:35
 **/
@Tag(name = "用户认证")
@RestController
@RequestMapping("/authorize")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final PasswordEncoder passwordEncoder;
    private final OnlineUserCacheService onlineUserCacheService;
    private final LogService logService;
    private final RedisService redisService;

//    @Operation(summary = "登录")
    @PostMapping("/login")
    public RespResult<Object> login(LoginDto loginDto) {
        String cacheCode = redisService.get(CacheConstant.LOGIN_CAPTCHA_KEY + loginDto.getCaptchaKey());
        if (StringUtils.isBlank(cacheCode)) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "验证码已失效");
            return RespResult.fail("验证码已失效");
        }
        // 不管正不正确，使用过的验证码都先删除，防止撞库
        redisService.delete(CacheConstant.LOGIN_CAPTCHA_KEY + loginDto.getCaptchaKey());
        if (!cacheCode.equals(loginDto.getCaptchaCode().toLowerCase())) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "验证码不正确");
            return RespResult.fail("验证码不正确");
        }

        User user = userService.findByUsername(loginDto.getUsername());
        if (user == null) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "用户名不存在");
            return RespResult.fail("用户名或密码错误");
        }
        if (user.getEnabled() == 0) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "账号被禁用");
            return RespResult.fail("账号未激活，请联系管理员!");
        }
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "密码错误");
            return RespResult.fail("用户名或密码错误");
        }

        // 构建登录用户信息
        LoginUserVO loginUserVO = LoginUserVO.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .avatar(user.getAvatar())
                .deptId(user.getDeptId())
                .isAdmin(user.getId() == 1L)
                .enabled(user.getEnabled() == 1)
                .uuid(IdUtil.fastSimpleUUID())
                .build();
        // 数据权限 deptId 集合
        List<Long> deptIds = roleService.getDeptIds(loginUserVO.getUserId(), loginUserVO.getDeptId());
        // 菜单权限列表
        List<String> permissions = menuService.getPermissionByUserId(loginUserVO.getUserId());
        // 角色标识列表
        List<String> roleCodeList = roleService.getRoleCodeList(loginUserVO.getUserId());
        loginUserVO.setDataScopes(deptIds);
        loginUserVO.setPermissions(permissions);
        loginUserVO.setRoles(roleCodeList);

        // sa-token 登录
        StpUtil.login(user.getId());
        // 登录用户信息缓存到会话
        StpUtil.getSession().set(CacheConstant.LOGIN_USER_KEY, loginUserVO);
        // 在线用户入库
        onlineUserCacheService.save(loginUserVO, 60, false);
        // 保存登陆日志
        logService.saveLoginLog(loginUserVO.getUserId(), loginUserVO.getUsername(), LoginResultEnum.LOGIN_SUCCESS.getValue(), "");

        Map<String, String> data = new HashMap<>();
        data.put("accessToken", "Bearer " + StpUtil.getTokenValue());
        return RespResult.success(data);
    }

//    @Operation(summary = "登出")
    @DeleteMapping("/logout")
    public RespResult<Object> logout() {
        Optional<LoginUserVO> loginUserVO = SecurityUtil.getLoginUserVO();
        Long userId = loginUserVO.map(LoginUserVO::getUserId).orElse(0L);
        String username = loginUserVO.map(LoginUserVO::getUsername).orElse("");
        String uuid = SecurityUtil.getUuid();
        logService.saveLoginLog(userId, username, LoginResultEnum.LOGOUT_SUCCESS.getValue(), "");
        onlineUserCacheService.logout(uuid);
        StpUtil.logout();
        return RespResult.success();
    }

    @Operation(summary = "获取验证码")
    @GetMapping("/captcha")
    public RespResult<Object> captcha() {
        // 算术类型验证码，更多类型可选：png、gif、中文、中文gif、简单算术类型
        ArithmeticCaptcha  captcha = new ArithmeticCaptcha(130, 48);
        // 几个数字运算，默认是两个
        captcha.setLen(2);
        // 可设置支持的算法：2 表示只生成带加减法的公式
        captcha.supportAlgorithmSign(2);
        // 设置计算难度，参与计算的每一个整数的最大值
        captcha.setDifficulty(20);
        // 运算公式转base64
        String base64 = captcha.toBase64();
        // 运算结果
        String code = captcha.text().toLowerCase();
        // redis缓存key
        String uuid = IdUtil.fastSimpleUUID();
        // 5分钟内有效
        redisService.setEx(CacheConstant.LOGIN_CAPTCHA_KEY + uuid, code, 5, TimeUnit.MINUTES);
        return RespResult.success(CaptchaVO.builder().captchaKey(uuid).captchaBase64(base64).build());
    }
}
