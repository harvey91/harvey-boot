package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.query.Query;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.service.NoticeService;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 系统通知表 前端控制器
 *
 * @author harvey
 * @since 2024-11-19
 */
@Tag(name = "系统通知表 Controller")
@RestController
@RequestMapping("/system/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "id查询表单", description = "根据配置id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<Notice> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(noticeService.getById(id));
    }

    @Operation(summary = "分页列表")
    @GetMapping("/page")
    public RespResult<PageResult<Notice>> page(Query query) {
        Page<Notice> page = noticeService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody Notice entity) {
        noticeService.save(entity);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody Notice entity) {
        noticeService.updateById(entity);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        noticeService.removeByIds(ids);
        return RespResult.success();
    }
}
