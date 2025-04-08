package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.query.NoticeUserQuery;
import com.harvey.system.model.entity.NoticeUser;
import com.harvey.system.service.NoticeUserService;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统通知指定用户表 前端控制器
 *
 * @author harvey
 * @since 2024-11-20
 */
@Tag(name = "通知公告指定用户")
@RestController
@RequestMapping("/system/noticeUser")
@RequiredArgsConstructor
public class NoticeUserController {
    private final NoticeUserService noticeUserService;

    @Operation(summary = "id查询表单", description = "根据配置id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<NoticeUser> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeUserService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:notice:user:list')")
    @GetMapping("/page")
    public RespResult<PageResult<NoticeUser>> page(NoticeUserQuery query) {
        Page<NoticeUser> page = noticeUserService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:notice:user:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody NoticeUser entity) {
        noticeUserService.save(entity);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:notice:user:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody NoticeUser entity) {
        noticeUserService.updateById(entity);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:notice:user:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        noticeUserService.removeByIds(ids);
        return RespResult.success();
    }
}
