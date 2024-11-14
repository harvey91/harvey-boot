package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysDict;
import com.harvey.system.entity.SysOnlineUser;
import com.harvey.system.security.OnlineUserService;
import com.harvey.system.service.ISysOnlineUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

/**
 * <p>
 * 系统在线用户表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-14
 */
@RestController
@RequestMapping("/system/onlineUser")
@RequiredArgsConstructor
public class SysOnlineUserController {
    private final ISysOnlineUserService sysOnlineUserService;
    private final OnlineUserService onlineUserService;

    @GetMapping("/page")
    public RespResult<PageResult<SysOnlineUser>> page(@RequestParam("pageNum") int pageNum,
                                                      @RequestParam("pageSize") int pageSize) {
        Page<SysOnlineUser> onlineUserPage = sysOnlineUserService.page(pageNum, pageSize);
        return RespResult.success(PageResult.of(onlineUserPage));
    }

    /**
     * 踢用户下线
     * @param uuid
     * @return
     */
    @PreAuthorize("@ex.hasPerm('sys:online:user:offline')")
    @DeleteMapping("/offline/{uuid}")
    public RespResult<String> offline(@PathVariable(value = "uuid") String uuid) {
        onlineUserService.delete(uuid);
        return RespResult.success();
    }
}
