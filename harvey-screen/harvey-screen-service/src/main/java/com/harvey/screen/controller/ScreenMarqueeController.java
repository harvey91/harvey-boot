package com.harvey.screen.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.screen.model.dto.ScreenMarqueeDto;
import com.harvey.screen.model.dto.ScreenMarqueeSendDto;
import com.harvey.screen.model.query.ScreenMarqueeQuery;
import com.harvey.screen.model.vo.ScreenCommandSendVO;
import com.harvey.screen.model.vo.ScreenMarqueeVO;
import com.harvey.screen.service.ScreenMarqueeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 信发滚动字幕模板 前端控制器
 *
 * @author Harvey
 */
@Tag(name = "信发滚动字幕模板")
@RestController
@RequestMapping("/screen/marquee")
@RequiredArgsConstructor
public class ScreenMarqueeController {

    private final ScreenMarqueeService screenMarqueeService;

    @Operation(summary = "分页列表")
    @SaCheckPermission("screen:marquee:list")
    @GetMapping("/page")
    public RespResult<PageResult<ScreenMarqueeVO>> page(ScreenMarqueeQuery query) {
        Page<ScreenMarqueeVO> page = screenMarqueeService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "id查询表单")
    @SaCheckPermission("screen:marquee:list")
    @GetMapping("/form/{id}")
    public RespResult<ScreenMarqueeVO> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(screenMarqueeService.getFormById(id));
    }

    @Operation(summary = "新增")
    @SaCheckPermission("screen:marquee:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated ScreenMarqueeDto dto) {
        screenMarqueeService.create(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @SaCheckPermission("screen:marquee:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated ScreenMarqueeDto dto) {
        screenMarqueeService.modify(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @SaCheckPermission("screen:marquee:delete")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        screenMarqueeService.deleteById(id);
        return RespResult.success();
    }

    @Operation(summary = "下发字幕到设备")
    @SaCheckPermission("screen:marquee:send")
    @PostMapping("/send")
    public RespResult<ScreenCommandSendVO> send(@RequestBody @Validated ScreenMarqueeSendDto dto) {
        return RespResult.success(screenMarqueeService.send(dto));
    }
}