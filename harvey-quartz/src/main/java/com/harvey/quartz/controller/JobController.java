package com.harvey.quartz.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.quartz.model.dto.JobDto;
import com.harvey.quartz.model.query.JobQuery;
import com.harvey.quartz.model.entity.Job;
import com.harvey.quartz.service.JobService;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务调度 前端控制器
 *
 * @author harvey
 * @since 2025-04-08
 */
@Tag(name = "定时任务调度")
@RestController
@RequestMapping("/system/job")
@RequiredArgsConstructor
public class JobController {
    private final JobService jobService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<Job> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(jobService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:job:list')")
    @GetMapping("/page")
    public RespResult<PageResult<Job>> page(JobQuery query) {
        Page<Job> page = jobService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:job:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated JobDto dto) {
        jobService.saveJob(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:job:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated JobDto dto) {
        jobService.updateJob(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:job:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        jobService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "修改状态")
    @PreAuthorize("@ex.hasPerm('sys:job:modify')")
    @PutMapping("/modify/status")
    public RespResult<String> status(@RequestBody @Validated JobDto dto) {
        jobService.updateJobStatus(dto);
        return RespResult.success();
    }

    @Operation(summary = "立即执行")
    @PreAuthorize("@ex.hasPerm('sys:job:run')")
    @PutMapping("/run")
    public RespResult<String> run(@RequestBody @Validated JobDto dto) {

        return RespResult.success();
    }
}
