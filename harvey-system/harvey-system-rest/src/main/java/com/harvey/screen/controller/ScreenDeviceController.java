package com.harvey.screen.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.screen.model.dto.ScreenDeviceDto;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.model.query.ScreenDeviceQuery;
import com.harvey.screen.model.vo.ScreenDeviceVO;
import com.harvey.screen.service.ScreenDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
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
 * 信发设备 前端控制器
 *
 * @author Harvey
 */
@Tag(name = "信发设备")
@RestController
@RequestMapping("/screen/device")
@RequiredArgsConstructor
public class ScreenDeviceController {

    private final ScreenDeviceService screenDeviceService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<ScreenDevice> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(screenDeviceService.getById(id));
    }

    @Operation(summary = "分页查询")
    @SaCheckPermission("screen:device:list")
    @GetMapping("/page")
    public RespResult<PageResult<ScreenDeviceVO>> page(ScreenDeviceQuery query) {
        Page<ScreenDeviceVO> page = screenDeviceService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "全部已启用设备(下拉/分组下发)")
    @SaCheckPermission("screen:device:list")
    @GetMapping("/list")
    public RespResult<List<ScreenDeviceVO>> list() {
        return RespResult.success(screenDeviceService.listEnabled());
    }

    @Operation(summary = "在线设备数")
    @SaCheckPermission("screen:device:list")
    @GetMapping("/online-count")
    public RespResult<Integer> onlineCount() {
        return RespResult.success(screenDeviceService.onlineCount());
    }

    @Operation(summary = "新增设备")
    @SaCheckPermission("screen:device:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated ScreenDeviceDto dto) {
        screenDeviceService.saveDevice(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改设备")
    @SaCheckPermission("screen:device:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated ScreenDeviceDto dto) {
        screenDeviceService.updateDevice(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除设备")
    @SaCheckPermission("screen:device:delete")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        screenDeviceService.deleteByIds(List.of(id));
        return RespResult.success();
    }

    @Operation(summary = "审核通过设备")
    @SaCheckPermission("screen:device:approve")
    @PutMapping("/approve/{id}")
    public RespResult<String> approve(@PathVariable(value = "id") Long id) {
        screenDeviceService.approve(id);
        return RespResult.success();
    }

    @Operation(summary = "审核拒绝设备")
    @SaCheckPermission("screen:device:reject")
    @PutMapping("/reject/{id}")
    public RespResult<String> reject(@PathVariable(value = "id") Long id) {
        screenDeviceService.reject(id);
        return RespResult.success();
    }
}