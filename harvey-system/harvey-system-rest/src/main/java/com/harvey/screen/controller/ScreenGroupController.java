package com.harvey.screen.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import com.harvey.screen.model.dto.ScreenDeviceGroupDto;
import com.harvey.screen.model.dto.ScreenGroupSendDto;
import com.harvey.screen.model.query.ScreenDeviceGroupQuery;
import com.harvey.screen.model.vo.ScreenDeviceGroupVO;
import com.harvey.screen.model.vo.ScreenGroupSendVO;
import com.harvey.screen.service.ScreenDeviceGroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 信发设备分组 前端控制器
 *
 * @author Harvey
 */
@Tag(name = "信发设备分组")
@RestController
@RequestMapping("/screen/group")
@RequiredArgsConstructor
public class ScreenGroupController {

    private final ScreenDeviceGroupService screenDeviceGroupService;

    @Operation(summary = "分页列表")
    @SaCheckPermission("screen:group:list")
    @GetMapping("/page")
    public RespResult<PageResult<ScreenDeviceGroupVO>> page(ScreenDeviceGroupQuery query) {
        Page<ScreenDeviceGroupVO> page = screenDeviceGroupService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "全部启用分组(下拉)")
    @SaCheckPermission("screen:group:list")
    @GetMapping("/list")
    public RespResult<List<ScreenDeviceGroupVO>> list() {
        return RespResult.success(screenDeviceGroupService.listAll());
    }

    @Operation(summary = "id查询表单")
    @SaCheckPermission("screen:group:list")
    @GetMapping("/form/{id}")
    public RespResult<ScreenDeviceGroupVO> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(screenDeviceGroupService.getFormById(id));
    }

    @Operation(summary = "新增")
    @SaCheckPermission("screen:group:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated ScreenDeviceGroupDto dto) {
        screenDeviceGroupService.create(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @SaCheckPermission("screen:group:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated ScreenDeviceGroupDto dto) {
        screenDeviceGroupService.modify(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @SaCheckPermission("screen:group:delete")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        screenDeviceGroupService.deleteById(id);
        return RespResult.success();
    }

    @Operation(summary = "分组批量下发(指令/字幕/媒体)")
    @SaCheckPermission("screen:group:send")
    @PostMapping("/send")
    public RespResult<ScreenGroupSendVO> send(@RequestBody @Validated ScreenGroupSendDto dto) {
        return RespResult.success(screenDeviceGroupService.sendToGroup(dto));
    }
}