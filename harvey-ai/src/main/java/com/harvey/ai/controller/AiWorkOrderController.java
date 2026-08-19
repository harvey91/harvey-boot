package com.harvey.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.ai.model.dto.AiWorkOrderDto;
import com.harvey.ai.model.query.AiWorkOrderQuery;
import com.harvey.ai.model.vo.AiWorkOrderVO;
import com.harvey.ai.service.AiWorkOrderService;
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
 * AI 客服工单 前端控制器
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Tag(name = "AI 客服工单")
@RestController
@RequestMapping("/ai/order")
@RequiredArgsConstructor
public class AiWorkOrderController {

    private final AiWorkOrderService workOrderService;

    @Operation(summary = "工单分页列表")
    @SaCheckPermission("ai:order:list")
    @GetMapping("/page")
    public RespResult<PageResult<AiWorkOrderVO>> page(AiWorkOrderQuery query) {
        Page<AiWorkOrderVO> page = workOrderService.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "按会话查询人工回复")
    @GetMapping("/reply")
    public RespResult<AiWorkOrderVO> replyByConversation(@RequestParam("conversationId") String conversationId) {
        return RespResult.success(workOrderService.getReplyByConversation(conversationId));
    }

    @Operation(summary = "人工回复工单")
    @SaCheckPermission("ai:order:reply")
    @PutMapping("/reply")
    public RespResult<String> reply(@RequestBody @Validated AiWorkOrderDto dto) {
        workOrderService.reply(dto.getId(), dto.getReply());
        return RespResult.success();
    }

    @Operation(summary = "关闭工单")
    @SaCheckPermission("ai:order:reply")
    @PutMapping("/close")
    public RespResult<String> close(@RequestBody AiWorkOrderDto dto) {
        if (dto.getId() == null) {
            return RespResult.fail("id不能为空");
        }
        workOrderService.close(dto.getId());
        return RespResult.success();
    }

    @Operation(summary = "删除工单")
    @SaCheckPermission("ai:order:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        workOrderService.deleteByIds(ids);
        return RespResult.success();
    }
}