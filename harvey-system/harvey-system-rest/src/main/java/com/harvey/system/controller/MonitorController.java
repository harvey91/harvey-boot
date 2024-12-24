package com.harvey.system.controller;

import com.harvey.common.result.RespResult;
import com.harvey.system.model.vo.server.ServerVO;
import com.harvey.system.service.MonitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Harvey
 * @date 2024-12-04 17:09
 **/
@Tag(name = "服务器监控")
@RestController
@RequestMapping("/system/monitor")
@RequiredArgsConstructor
public class MonitorController {
    private final MonitorService monitorService;

    @Operation(summary = "监控信息")
    @GetMapping("/info")
    @PreAuthorize("@ex.hasPerm('system:minotor:info')")
    public RespResult<ServerVO> info() {
        return RespResult.success(monitorService.getServers());
    }
}
