package com.harvey.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.ai.model.dto.AiFaqDto;
import com.harvey.ai.model.query.AiFaqQuery;
import com.harvey.ai.model.vo.AiFaqVO;
import com.harvey.ai.service.AiFaqService;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * AI 客服常见问题 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Tag(name = "AI 客服常见问题")
@RestController
@RequestMapping("/ai/faq")
@RequiredArgsConstructor
public class AiFaqController {

    private final AiFaqService faqService;

    @Operation(summary = "常见问题分页列表")
    @SaCheckPermission("ai:faq:list")
    @GetMapping("/page")
    public RespResult<PageResult<AiFaqVO>> page(AiFaqQuery query) {
        Page<AiFaqVO> page = faqService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增常见问题")
    @SaCheckPermission("ai:faq:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated AiFaqDto dto) {
        faqService.saveFaq(dto);
        return RespResult.success();
    }

    @Operation(summary = "编辑常见问题")
    @SaCheckPermission("ai:faq:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated AiFaqDto dto) {
        faqService.updateFaq(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除常见问题")
    @SaCheckPermission("ai:faq:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        faqService.deleteByIds(ids);
        return RespResult.success();
    }
}