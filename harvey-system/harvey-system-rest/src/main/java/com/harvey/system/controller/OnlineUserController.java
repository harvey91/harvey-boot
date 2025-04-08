package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import com.harvey.system.model.entity.OnlineUser;
import com.harvey.system.security.service.OnlineUserCacheService;
import com.harvey.system.service.OnlineUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 系统在线用户表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-14
 */
@Tag(name = "在线用户")
@RestController
@RequestMapping("/system/onlineUser")
@RequiredArgsConstructor
public class OnlineUserController {
    private final OnlineUserService onlineUserService;
    private final OnlineUserCacheService onlineUserCacheService;

    @Operation(summary = "在线用户列表")
    @PreAuthorize("@ex.hasPerm('sys:online:user:list')")
    @GetMapping("/page")
    public RespResult<PageResult<OnlineUser>> page(@RequestParam("pageNum") int pageNum,
                                                      @RequestParam("pageSize") int pageSize) {
        Page<OnlineUser> onlineUserPage = onlineUserService.page(pageNum, pageSize);
        return RespResult.success(PageResult.of(onlineUserPage));
    }

    @Operation(summary = "强制用户下线")
    @PreAuthorize("@ex.hasPerm('sys:online:user:offline')")
    @DeleteMapping("/offline/{uuid}")
    public RespResult<String> offline(@PathVariable(value = "uuid") String uuid) {
        onlineUserCacheService.delete(uuid);
        return RespResult.success();
    }
}
