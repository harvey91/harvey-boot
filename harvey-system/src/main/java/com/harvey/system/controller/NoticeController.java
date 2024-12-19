package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.result.PageResult;
import com.harvey.common.base.RespResult;
import com.harvey.system.model.dto.NoticeDto;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.model.query.NoticeQuery;
import com.harvey.system.model.query.NoticeUserQuery;
import com.harvey.system.model.vo.NoticeUserVO;
import com.harvey.system.model.vo.NoticeVO;
import com.harvey.system.service.NoticeService;
import com.harvey.system.service.NoticeUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统通知表 前端控制器
 *
 * @author harvey
 * @since 2024-11-20
 */
@Tag(name = "通知公告")
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;
    private final NoticeUserService noticeUserService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<Notice> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeService.getNoticeById(id));
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
    @PatchMapping("/publish/{id}")
    public RespResult<String> publish(@PathVariable(value = "id") Long id) {
        noticeService.publish(id);
        return RespResult.success();
    }

    @Operation(summary = "撤回")
    @PreAuthorize("@ex.hasPerm('sys:notice:revoke')")
    @PatchMapping("/revoke/{id}")
    public RespResult<String> revoke(@PathVariable(value = "id") Long id) {
        noticeService.revoke(id);
        return RespResult.success();
    }

    @Operation(summary = "查看详情", description = "包含通知内容text字段")
    @GetMapping("/detail/{id}")
    public RespResult<NoticeVO> detailById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeService.detail(id));
    }

    @Operation(summary = "全部已读")
    @PutMapping("/read-all")
    public RespResult<String> readAll() {
        noticeUserService.readAll();
        return RespResult.success();
    }

    @Operation(summary = "我的通知分页列表")
    @GetMapping("/my-page")
    public RespResult<PageResult<NoticeUserVO>> myPage(NoticeUserQuery query) {
        Page<NoticeUserVO> noticeUserVOPage = noticeUserService.myPage(query);
        return RespResult.success(PageResult.of(noticeUserVOPage));
    }
}
