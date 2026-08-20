package com.harvey.screen.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.screen.model.dto.ScreenCommandDto;
import com.harvey.screen.model.query.ScreenCommandQuery;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import com.harvey.screen.model.vo.ScreenCommandVO;
import com.harvey.screen.service.ScreenCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 信发指令 前端控制器
 *
 * @author Harvey
 */
@Tag(name = "信发指令")
@RestController
@RequestMapping("/screen/command")
@RequiredArgsConstructor
public class ScreenCommandController {

    private final ScreenCommandService screenCommandService;

    @Operation(summary = "分页查询指令记录")
    @SaCheckPermission("screen:command:list")
    @GetMapping("/page")
    public RespResult<PageResult<ScreenCommandVO>> page(ScreenCommandQuery query) {
        Page<ScreenCommandVO> page = screenCommandService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "下发指令")
    @SaCheckPermission("screen:command:send")
    @PostMapping("/send")
    public RespResult<ScreenCommandSendVO> send(@RequestBody @Validated ScreenCommandDto dto) {
        return RespResult.success(screenCommandService.sendCommand(dto));
    }
}