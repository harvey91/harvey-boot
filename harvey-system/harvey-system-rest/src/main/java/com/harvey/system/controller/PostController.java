package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.core.model.PageResult;
import com.harvey.common.result.RespResult;
import com.harvey.system.model.dto.PostDto;
import com.harvey.system.model.entity.Post;
import com.harvey.system.model.query.PostQuery;
import com.harvey.system.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 职位管理 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Tag(name = "职位管理")
@RestController
@RequestMapping("/system/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<Post> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(postService.getById(id));
    }

    @Operation(summary = "职位分页列表")
    @SaCheckPermission("sys:post:list")
    @GetMapping("/page")
    public RespResult<PageResult<Post>> page(PostQuery query) {
        Page<Post> dictPage = postService.queryPage(query);
        return RespResult.success(PageResult.of(dictPage));
    }

    @Operation(summary = "新增职位")
    @SaCheckPermission("sys:post:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated PostDto dto) {
        postService.savePost(dto);
        return RespResult.success();
    }

    @Operation(summary = "编辑职位")
    @SaCheckPermission("sys:post:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated PostDto dto) {
        postService.updatePost(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除职位")
    @SaCheckPermission("sys:post:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        postService.deleteByIds(ids);
        return RespResult.success();
    }
}
