package com.harvey.ai.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.harvey.ai.model.query.AiConversationQuery;
import com.harvey.ai.model.vo.AiChatMessageVO;
import com.harvey.ai.model.vo.AiConversationVO;
import com.harvey.ai.service.AiChatRecordService;
import com.harvey.common.result.RespResult;
import com.harvey.core.model.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI 客服会话记录 前端控制器
 *
 * @author harvey
 * @since 2026-08-18
 */
@Tag(name = "AI 客服会话记录")
@RestController
@RequestMapping("/ai/chatRecord")
@RequiredArgsConstructor
public class AiChatRecordController {

    private final AiChatRecordService chatRecordService;

    @Operation(summary = "会话分页列表")
    @SaCheckPermission("ai:chat:list")
    @GetMapping("/conversation/page")
    public RespResult<PageResult<AiConversationVO>> conversationPage(AiConversationQuery query) {
        return RespResult.success(chatRecordService.queryConversationPage(query));
    }

    @Operation(summary = "会话消息列表")
    @SaCheckPermission("ai:chat:list")
    @GetMapping("/message/list")
    public RespResult<List<AiChatMessageVO>> messageList(@RequestParam("conversationId") String conversationId) {
        return RespResult.success(chatRecordService.listMessages(conversationId));
    }

    @Operation(summary = "删除会话(含消息)")
    @SaCheckPermission("ai:chat:delete")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<String> conversationIds) {
        if (ObjectUtils.isEmpty(conversationIds)) {
            return RespResult.fail("请选择要删除的会话");
        }
        chatRecordService.deleteByConversationIds(conversationIds);
        return RespResult.success();
    }
}
