package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.model.dto.FileManageDto;
import com.harvey.system.model.entity.FileManage;
import com.harvey.system.model.query.FileManageQuery;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.service.FileManageService;
import com.harvey.system.storage.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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
    private final StorageService storageService;

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
    public RespResult<String> create(@RequestParam("file") MultipartFile file) throws IOException {
        Long userId = SecurityUtil.getUserId();
        storageService.store(file, userId);
        return RespResult.success();
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
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        fileManageService.deleteByIds(ids);
        return RespResult.success();
    }
}
