package com.harvey.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.harvey.common.result.RespResult;
import com.harvey.system.model.ratelimit.BanIpDTO;
import com.harvey.system.model.ratelimit.IpBanVO;
import com.harvey.system.model.ratelimit.RateLimitConfig;
import com.harvey.system.service.RateLimitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 限流与封IP 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Tag(name = "限流与封IP")
@RestController
@RequestMapping("/system/rateLimit")
@RequiredArgsConstructor
public class RateLimitController {

    private final RateLimitService rateLimitService;

    @Operation(summary = "获取限流配置")
    @SaCheckPermission("sys:rate:limit:config")
    @GetMapping("/config")
    public RespResult<RateLimitConfig> getConfig() {
        return RespResult.success(rateLimitService.getConfig());
    }

    @Operation(summary = "修改限流配置")
    @SaCheckPermission("sys:rate:limit:config")
    @PutMapping("/config")
    public RespResult<String> updateConfig(@RequestBody @Valid RateLimitConfig config) {
        rateLimitService.updateConfig(config);
        return RespResult.success();
    }

    @Operation(summary = "封禁IP列表")
    @SaCheckPermission("sys:rate:limit:list")
    @GetMapping("/blacklist")
    public RespResult<List<IpBanVO>> blacklist() {
        return RespResult.success(rateLimitService.listBlacklist());
    }

    @Operation(summary = "手动封禁IP")
    @SaCheckPermission("sys:rate:limit:ban")
    @PostMapping("/blacklist")
    public RespResult<String> ban(@RequestBody @Valid BanIpDTO dto) {
        rateLimitService.ban(dto.getIp(), dto.getBanSeconds());
        return RespResult.success();
    }

    @Operation(summary = "解封IP")
    @SaCheckPermission("sys:rate:limit:unban")
    @DeleteMapping("/blacklist/{ip}")
    public RespResult<String> unban(@PathVariable("ip") String ip) {
        rateLimitService.unban(ip);
        return RespResult.success();
    }
}
