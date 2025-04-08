package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.common.result.RespResult;
import com.harvey.common.enums.PlatformEnum;
import com.harvey.core.model.PageResult;
import com.harvey.storage.model.dto.FileManageDto;
import com.harvey.storage.model.entity.FileManage;
import com.harvey.storage.model.query.FileManageQuery;
import com.harvey.system.security.SecurityUtil;
import com.harvey.storage.service.FileManageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 文件管理 前端控制器
 *
 * @author harvey
 * @since 2024-12-05
 */
@Tag(name = "文件管理")
@RestController
@RequestMapping("/system/file/manage")
@RequiredArgsConstructor
public class FileManageController {
    private final FileManageService fileManageService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<FileManage> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(fileManageService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:file:manage:list')")
    @GetMapping("/page")
    public RespResult<PageResult<FileManage>> page(FileManageQuery query) {
        Page<FileManage> page = fileManageService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:file:manage:create')")
    @PostMapping("/create")
    public RespResult<FileManageDto> create(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = SecurityUtil.getUserId();
        FileManageDto manageDto = fileManageService.saveFileManage(file, userId, PlatformEnum.SYSTEM.name());
        return RespResult.success(manageDto);
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:file:manage:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated FileManageDto dto) {
        fileManageService.updateFileManage(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:file:manage:delete')")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@PathVariable(value = "id") Long id) {
        fileManageService.deleteById(id);
        return RespResult.success();
    }
}
