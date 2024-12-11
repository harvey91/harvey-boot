package com.harvey.system.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.model.dto.VerifyCodeDto;
import com.harvey.system.model.query.VerifyCodeQuery;
import com.harvey.system.model.entity.VerifyCode;
import com.harvey.system.service.VerifyCodeService;
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 验证码 前端控制器
 *
 * @author harvey
 * @since 2024-12-11
 */
@Tag(name = "验证码")
@RestController
@RequestMapping("/system/verifyCode")
@RequiredArgsConstructor
public class VerifyCodeController {
    private final VerifyCodeService verifyCodeService;

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<VerifyCode> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(verifyCodeService.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('sys:verify:code:list')")
    @GetMapping("/page")
    public RespResult<PageResult<VerifyCode>> page(VerifyCodeQuery query) {
        Page<VerifyCode> page = verifyCodeService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('sys:verify:code:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated VerifyCodeDto dto) {
        verifyCodeService.saveVerifyCode(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('sys:verify:code:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated VerifyCodeDto dto) {
        verifyCodeService.updateVerifyCode(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('sys:verify:code:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        verifyCodeService.deleteByIds(ids);
        return RespResult.success();
    }
}
