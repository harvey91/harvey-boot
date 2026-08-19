package com.harvey.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.mapper.AiWorkOrderMapper;
import com.harvey.ai.mapstruct.AiWorkOrderConverter;
import com.harvey.ai.model.entity.AiWorkOrder;
import com.harvey.ai.model.query.AiWorkOrderQuery;
import com.harvey.ai.model.vo.AiWorkOrderVO;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * AI客服工单 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Service
@RequiredArgsConstructor
public class AiWorkOrderService extends ServiceImpl<AiWorkOrderMapper, AiWorkOrder> {

    private final AiWorkOrderConverter converter;

    public Page<AiWorkOrderVO> queryPage(AiWorkOrderQuery query) {
        Page<AiWorkOrder> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AiWorkOrder> wrapper = new LambdaQueryWrapper<AiWorkOrder>()
                .eq(query.getStatus() != null, AiWorkOrder::getStatus, query.getStatus())
                .orderByAsc(AiWorkOrder::getStatus)
                .orderByDesc(AiWorkOrder::getCreateTime);
        Page<AiWorkOrder> result = this.page(page, wrapper);
        Page<AiWorkOrderVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(converter::toVO).toList());
        return voPage;
    }

    /**
     * 客户留言/转人工, 生成待处理工单
     */
    public Long createOrder(String conversationId, String contact, String content) {
        if (StringUtils.isBlank(content)) {
            throw new BusinessException("留言内容不能为空");
        }
        AiWorkOrder order = new AiWorkOrder();
        order.setConversationId(conversationId);
        order.setContact(contact);
        order.setContent(content);
        order.setStatus(0);
        this.save(order);
        return order.getId();
    }

    /**
     * 人工回复, 状态置为已回复
     */
    public void reply(Long id, String reply) {
        AiWorkOrder order = getOrder(id);
        if (order.getStatus() != null && order.getStatus() == 2) {
            throw new BusinessException("工单已关闭, 无法回复");
        }
        order.setReply(reply);
        order.setStatus(1);
        order.setReplyTime(LocalDateTime.now());
        this.updateById(order);
    }

    /**
     * 关闭工单
     */
    public void close(Long id) {
        AiWorkOrder order = getOrder(id);
        order.setStatus(2);
        this.updateById(order);
    }

    public void deleteByIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BusinessException("请选择要删除的工单");
        }
        this.removeByIds(ids);
    }

    /**
     * 按会话查询最新一条已有人工回复的工单(客户可见)
     */
    public AiWorkOrderVO getReplyByConversation(String conversationId) {
        if (StringUtils.isBlank(conversationId)) {
            return null;
        }
        AiWorkOrder order = this.getOne(new LambdaQueryWrapper<AiWorkOrder>()
                .eq(AiWorkOrder::getConversationId, conversationId)
                .isNotNull(AiWorkOrder::getReply)
                .orderByDesc(AiWorkOrder::getReplyTime)
                .last("LIMIT 1"));
        return order == null ? null : converter.toVO(order);
    }

    private AiWorkOrder getOrder(Long id) {
        AiWorkOrder order = id == null ? null : this.getById(id);
        if (order == null) {
            throw new BusinessException("工单不存在");
        }
        return order;
    }
}