package com.harvey.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.ai.model.dto.AiKnowledgeBaseDto;
import com.harvey.ai.model.query.AiKnowledgeBaseQuery;
import com.harvey.ai.model.vo.AiKnowledgeBaseVO;
import com.harvey.ai.service.AiKnowledgeBaseService;
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
 * AI 知识库 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Tag(name = "AI 知识库")
@RestController
@RequestMapping("/ai/knowledgeBase")
@RequiredArgsConstructor
public class AiKnowledgeBaseController {

    private final AiKnowledgeBaseService knowledgeBaseService;

    @Operation(summary = "知识库分页列表")
    @SaCheckPermission("ai:knowledge:list")
    @GetMapping("/page")
    public RespResult<PageResult<AiKnowledgeBaseVO>> page(AiKnowledgeBaseQuery query) {
        Page<AiKnowledgeBaseVO> page = knowledgeBaseService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "启用知识库列表")
    @SaCheckPermission("ai:knowledge:list")
    @GetMapping("/list")
    public RespResult<List<AiKnowledgeBaseVO>> list() {
        return RespResult.success(knowledgeBaseService.listEnabled());
    }

    @Operation(summary = "新增知识库")
    @SaCheckPermission("ai:knowledge:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated AiKnowledgeBaseDto dto) {
        knowledgeBaseService.saveKnowledgeBase(dto);
        return RespResult.success();
    }

    @Operation(summary = "编辑知识库")
    @SaCheckPermission("ai:knowledge:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated AiKnowledgeBaseDto dto) {
        knowledgeBaseService.updateKnowledgeBase(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除知识库")
    @SaCheckPermission("ai:knowledge:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        knowledgeBaseService.deleteByIds(ids);
        return RespResult.success();
    }
}