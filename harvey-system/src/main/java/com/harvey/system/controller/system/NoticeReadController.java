package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.query.NoticeReadQuery;
import com.harvey.system.model.entity.NoticeRead;
import com.harvey.system.service.NoticeReadService;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统通知已读用户表 前端控制器
 *
 * @author harvey
 * @since 2024-11-20
 */
@Tag(name = "系统通知已读用户表 Controller")
@RestController
@RequestMapping("/system/noticeRead")
@RequiredArgsConstructor
public class NoticeReadController {
    private final NoticeReadService noticeReadService;

    @Operation(summary = "id查询表单", description = "根据配置id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<NoticeRead> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeReadService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:notice:read:list')")
    @GetMapping("/page")
    public RespResult<PageResult<NoticeRead>> page(NoticeReadQuery query) {
        Page<NoticeRead> page = noticeReadService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:notice:read:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody NoticeRead entity) {
        noticeReadService.save(entity);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:notice:read:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody NoticeRead entity) {
        noticeReadService.updateById(entity);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:notice:read:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        noticeReadService.removeByIds(ids);
        return RespResult.success();
    }
}
