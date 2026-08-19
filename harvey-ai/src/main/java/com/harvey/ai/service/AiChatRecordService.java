package com.harvey.ai.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.mapper.AiChatMessageMapper;
import com.harvey.ai.mapper.AiConversationMapper;
import com.harvey.ai.model.entity.AiChatMessage;
import com.harvey.ai.model.entity.AiConversation;
import com.harvey.ai.model.query.AiConversationQuery;
import com.harvey.ai.model.vo.AiChatMessageVO;
import com.harvey.ai.model.vo.AiConversationVO;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import com.harvey.core.model.PageResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * AI客服会话记录 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiChatRecordService {

    private final AiConversationMapper conversationMapper;
    private final AiChatMessageMapper chatMessageMapper;

    /**
     * 记录一轮问答: 自动创建/更新会话, 写入用户消息与机器人消息
     *
     * @param title      会话标题(首问摘要, 仅新建时生效)
     */
    @Transactional(rollbackFor = Throwable.class)
    public void record(String conversationId, String title, String userMessage,
                       String botContent, List<AiCustomerService.CustomerCitation> citations, boolean needHuman) {
        if (StringUtils.isBlank(conversationId)) {
            return;
        }
        AiConversation conversation = getConversation(conversationId);
        LocalDateTime now = LocalDateTime.now();
        if (conversation == null) {
            conversation = new AiConversation();
            conversation.setConversationId(conversationId);
            conversation.setTitle(StringUtils.abbreviate(title, 50));
            conversation.setStatus(0);
            conversation.setMessageCount(1);
            conversation.setLastMessageTime(now);
            conversationMapper.insert(conversation);
        } else {
            conversation.setStatus(0);
            conversation.setLastMessageTime(now);
            conversation.setMessageCount((conversation.getMessageCount() == null ? 0 : conversation.getMessageCount()) + 1);
            conversationMapper.updateById(conversation);
        }

        if (StringUtils.isNotBlank(userMessage)) {
            AiChatMessage userMsg = buildMessage(conversationId, "USER", userMessage, null, false);
            chatMessageMapper.insert(userMsg);
        }
        if (StringUtils.isNotBlank(botContent)) {
            AiChatMessage botMsg = buildMessage(conversationId, "BOT", botContent,
                    citations == null ? null : JSONUtil.toJsonStr(citations), needHuman);
            chatMessageMapper.insert(botMsg);
        }
    }

    private AiChatMessage buildMessage(String conversationId, String role, String content,
                                       String citations, boolean needHuman) {
        AiChatMessage msg = new AiChatMessage();
        msg.setConversationId(conversationId);
        msg.setRole(role);
        msg.setContent(content);
        msg.setCitations(citations);
        msg.setNeedHuman(needHuman ? 1 : 0);
        msg.setRating(0);
        return msg;
    }

    public AiConversation getConversation(String conversationId) {
        return conversationMapper.selectOne(new LambdaQueryWrapper<AiConversation>()
                .eq(AiConversation::getConversationId, conversationId)
                .last("LIMIT 1"));
    }

    public PageResult<AiConversationVO> queryConversationPage(AiConversationQuery query) {
        Page<AiConversation> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AiConversation> wrapper = new LambdaQueryWrapper<AiConversation>()
                .like(StringUtils.isNotBlank(query.getKeywords()), AiConversation::getTitle, query.getKeywords())
                .eq(query.getStatus() != null, AiConversation::getStatus, query.getStatus())
                .orderByDesc(AiConversation::getLastMessageTime)
                .orderByDesc(AiConversation::getId);
        Page<AiConversation> result = conversationMapper.selectPage(page, wrapper);
        List<AiConversationVO> vos = result.getRecords().stream().map(c -> {
            AiConversationVO vo = new AiConversationVO();
            vo.setId(c.getId());
            vo.setConversationId(c.getConversationId());
            vo.setTitle(c.getTitle());
            vo.setMessageCount(c.getMessageCount());
            vo.setLastMessageTime(c.getLastMessageTime());
            vo.setCreateTime(c.getCreateTime());
            return vo;
        }).toList();
        Page<AiConversationVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(vos);
        return PageResult.of(voPage);
    }

    public List<AiChatMessageVO> listMessages(String conversationId) {
        if (StringUtils.isBlank(conversationId)) {
            return List.of();
        }
        List<AiChatMessage> messages = chatMessageMapper.selectList(new LambdaQueryWrapper<AiChatMessage>()
                .eq(AiChatMessage::getConversationId, conversationId)
                .orderByAsc(AiChatMessage::getId));
        return messages.stream().map(m -> {
            AiChatMessageVO vo = new AiChatMessageVO();
            vo.setId(m.getId());
            vo.setConversationId(m.getConversationId());
            vo.setRole(m.getRole());
            vo.setContent(m.getContent());
            try {
                vo.setCitations(com.alibaba.fastjson2.JSON.parseArray(m.getCitations(), AiCustomerService.CustomerCitation.class));
            } catch (Exception e) {
                vo.setCitations(null);
            }
            vo.setNeedHuman(m.getNeedHuman());
            vo.setRating(m.getRating());
            vo.setRatingTime(m.getRatingTime());
            vo.setCreateTime(m.getCreateTime());
            return vo;
        }).toList();
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByConversationIds(List<String> conversationIds) {
        if (conversationIds == null || conversationIds.isEmpty()) {
            throw new BusinessException("请选择要删除的会话");
        }
        conversationMapper.delete(new LambdaQueryWrapper<AiConversation>()
                .in(AiConversation::getConversationId, conversationIds));
        chatMessageMapper.delete(new LambdaQueryWrapper<AiChatMessage>()
                .in(AiChatMessage::getConversationId, conversationIds));
    }

    /**
     * 对会话最后一条机器人消息进行评价
     */
    @Transactional(rollbackFor = Throwable.class)
    public void rate(String conversationId, int rating) {
        if (StringUtils.isBlank(conversationId)) {
            throw new BusinessException("会话ID不能为空");
        }
        AiChatMessage lastBot = chatMessageMapper.selectOne(new LambdaQueryWrapper<AiChatMessage>()
                .eq(AiChatMessage::getConversationId, conversationId)
                .eq(AiChatMessage::getRole, "BOT")
                .orderByDesc(AiChatMessage::getId)
                .last("LIMIT 1"));
        if (lastBot == null) {
            throw new BusinessException("会话暂无机器人回答, 无法评价");
        }
        chatMessageMapper.update(null, new LambdaUpdateWrapper<AiChatMessage>()
                .eq(AiChatMessage::getId, lastBot.getId())
                .set(AiChatMessage::getRating, rating)
                .set(AiChatMessage::getRatingTime, LocalDateTime.now()));
    }

    public String newConversationId() {
        return IdUtil.fastSimpleUUID();
    }
}
