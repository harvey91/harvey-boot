package com.harvey.system.controller;

import cn.hutool.core.util.IdUtil;
import com.harvey.common.base.RespResult;
import com.harvey.common.constant.CacheConstant;
import com.harvey.common.enums.LoginResultEnum;
import com.harvey.system.model.dto.LoginDto;
import com.harvey.system.model.vo.CaptchaVO;
import com.harvey.system.redis.RedisCache;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.security.service.JwtTokenService;
import com.harvey.system.security.service.OnlineUserCacheService;
import com.harvey.system.service.LogService;
import com.harvey.system.utils.StringUtils;
import com.pig4cloud.captcha.ArithmeticCaptcha;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final OnlineUserCacheService onlineUserCacheService;
    private final LogService logService;
    private final RedisCache redisCache;

//    @Operation(summary = "登录")
    @PostMapping("/login")
    public RespResult<Object> login(LoginDto loginDto) {
        String cacheCode = redisCache.get(CacheConstant.LOGIN_CAPTCHA_KEY + loginDto.getCaptchaKey());
        if (StringUtils.isBlank(cacheCode)) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "验证码已失效");
            return RespResult.fail("验证码已失效");
        }
        // 不管正不正确，使用过的验证码都先删除，防止撞库
        redisCache.delete(CacheConstant.LOGIN_CAPTCHA_KEY + loginDto.getCaptchaKey());
        if (!cacheCode.equals(loginDto.getCaptchaCode().toLowerCase())) {
            logService.saveLoginLog(0L, loginDto.getUsername(), LoginResultEnum.LOGIN_FAILED.getValue(), "验证码不正确");
            return RespResult.fail("验证码不正确");
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = authenticationManager.authenticate(authenticationToken);
        String token = jwtTokenService.createToken(authentication);
        Map<String, String> data = new HashMap<>();
        data.put("accessToken", "Bearer " + token);
        return RespResult.success(data);
    }

//    @Operation(summary = "登出")
    @DeleteMapping("/logout")
    public RespResult<Object> logout() {
        Optional<LoginUserVO> loginUserVO = SecurityUtil.getLoginUserVO();
        Long userId = loginUserVO.map(LoginUserVO::getUserId).orElse(0L);
        String username = loginUserVO.map(LoginUserVO::getUsername).orElse("");
        logService.saveLoginLog(userId, username, LoginResultEnum.LOGOUT_SUCCESS.getValue(), "");
        onlineUserCacheService.delete(SecurityUtil.getUuid());
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
        redisCache.setEx(CacheConstant.LOGIN_CAPTCHA_KEY + uuid, code, 5, TimeUnit.MINUTES);
        return RespResult.success(CaptchaVO.builder().captchaKey(uuid).captchaBase64(base64).build());
    }
}
