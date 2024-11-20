package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.dto.NoticeDto;
import com.harvey.system.model.query.NoticeQuery;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.service.NoticeService;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统通知表 前端控制器
 *
 * @author harvey
 * @since 2024-11-20
 */
@Tag(name = "系统通知表 Controller")
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<Notice> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:notice:list')")
    @GetMapping("/page")
    public RespResult<PageResult<Notice>> page(NoticeQuery query) {
        Page<Notice> page = noticeService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:notice:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated NoticeDto dto) {
        noticeService.saveNotice(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:notice:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated NoticeDto dto) {
        noticeService.updateNotice(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:notice:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        noticeService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "发布")
    @PreAuthorize("@ex.hasPerm('sys:notice:publish')")
    @PatchMapping("/publish")
    public RespResult<String> publish() {

        return RespResult.success();
    }

    @Operation(summary = "撤回")
    @PreAuthorize("@ex.hasPerm('sys:notice:revoke')")
    @PatchMapping("/revoke")
    public RespResult<String> revoke() {

        return RespResult.success();
    }

    @Operation(summary = "id查询详情", description = "包含通知内容text字段")
    @GetMapping("/detail/{id}")
    public RespResult<Notice> detailById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeService.getById(id));
    }

    @Operation(summary = "全部已读")
    @PutMapping("/read-all")
    public RespResult<String> readAll() {

        return RespResult.success();
    }

    @Operation(summary = "我的通知分页列表")
    @GetMapping("/my-page")
    public RespResult<Object> myPage() {

        return RespResult.success();
    }
}
