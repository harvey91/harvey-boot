package com.harvey.system.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import com.harvey.system.entity.SysPost;
import com.harvey.system.service.ISysPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统职位表 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@RestController
@RequestMapping("/system/post")
@RequiredArgsConstructor
public class SysPostController {
    private final ISysPostService sysPostService;

    @GetMapping("/form/{id}")
    public RespResult<SysPost> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(sysPostService.getById(id));
    }

    @PreAuthorize("@ex.hasPerm('sys:post:list')")
    @GetMapping("/page")
    public RespResult<PageResult<SysPost>> page(@RequestParam("pageNum") int pageNum,
                                                @RequestParam("pageSize") int pageSize) {
        // TODO 查询条件
        Page<SysPost> page = new Page<>(pageNum, pageSize);
        Page<SysPost> dictPage = sysPostService.page(page);
        return RespResult.success(PageResult.of(dictPage));
    }

    @PreAuthorize("@ex.hasPerm('sys:post:create')")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody SysPost sysPost) {
        sysPostService.save(sysPost);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:post:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody SysPost sysPost) {
        sysPostService.updateById(sysPost);
        return RespResult.success();
    }

    @PreAuthorize("@ex.hasPerm('sys:post:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        sysPostService.removeByIds(ids);
        return RespResult.success();
    }
}
