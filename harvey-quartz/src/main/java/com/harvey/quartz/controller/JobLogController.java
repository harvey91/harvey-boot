package com.harvey.quartz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.quartz.model.dto.JobLogDto;
import com.harvey.quartz.model.query.JobLogQuery;
import com.harvey.quartz.model.entity.JobLog;
import com.harvey.quartz.service.JobLogService;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务调度日志 前端控制器
 *
 * @author harvey
 * @since 2025-04-08
 */
@Tag(name = "定时任务调度日志")
@RestController
@RequestMapping("/system/jobLog")
@RequiredArgsConstructor
public class JobLogController {
    private final JobLogService jobLogService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<JobLog> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(jobLogService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:job:log:list')")
    @GetMapping("/page")
    public RespResult<PageResult<JobLog>> page(JobLogQuery query) {
        Page<JobLog> page = jobLogService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:job:log:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated JobLogDto dto) {
        jobLogService.saveJobLog(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:job:log:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated JobLogDto dto) {
        jobLogService.updateJobLog(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:job:log:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        jobLogService.deleteByIds(ids);
        return RespResult.success();
    }
}
