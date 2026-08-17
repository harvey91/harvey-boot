package com.harvey.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.ai.model.dto.AiModelConfigDto;
import com.harvey.ai.model.entity.AiModelConfig;
import com.harvey.ai.model.query.AiModelConfigQuery;
import com.harvey.ai.model.vo.AiModelConfigVO;
import com.harvey.ai.service.AiModelConfigService;
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
 * AI 模型配置 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Tag(name = "AI 模型配置")
@RestController
@RequestMapping("/ai/model")
@RequiredArgsConstructor
public class AiModelConfigController {

    private final AiModelConfigService modelConfigService;

    @Operation(summary = "模型分页列表")
    @SaCheckPermission("ai:model:list")
    @GetMapping("/page")
    public RespResult<PageResult<AiModelConfigVO>> page(AiModelConfigQuery query) {
        Page<AiModelConfigVO> page = modelConfigService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "启用模型列表")
    @SaCheckPermission("ai:model:list")
    @GetMapping("/list")
    public RespResult<List<AiModelConfigVO>> list() {
        return RespResult.success(modelConfigService.listEnabled());
    }

    @Operation(summary = "id查询表单")
    @GetMapping("/form/{id}")
    public RespResult<AiModelConfigVO> formById(@PathVariable(value = "id") Long id) {
        AiModelConfig entity = modelConfigService.getById(id);
        return RespResult.success(modelConfigService.convertToVO(entity));
    }

    @Operation(summary = "新增模型")
    @SaCheckPermission("ai:model:create")
    @PostMapping("/create")
    public RespResult<String> create(@RequestBody @Validated AiModelConfigDto dto) {
        modelConfigService.saveConfig(dto);
        return RespResult.success();
    }

    @Operation(summary = "编辑模型")
    @SaCheckPermission("ai:model:modify")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated AiModelConfigDto dto) {
        modelConfigService.updateConfig(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除模型")
    @SaCheckPermission("ai:model:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        modelConfigService.deleteByIds(ids);
        return RespResult.success();
    }

    @Operation(summary = "设为默认模型")
    @SaCheckPermission("ai:model:modify")
    @PostMapping("/default/{id}")
    public RespResult<String> setDefault(@PathVariable(value = "id") Long id) {
        modelConfigService.setDefault(id);
        return RespResult.success();
    }
}