package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.query.Query;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.mapper.NoticeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * 系统通知表 服务实现类
 *
 * @author harvey
 * @since 2024-11-19
 */
@Service
@RequiredArgsConstructor
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {
    private final NoticeMapper mapper;

    public Page<Notice> queryPage(Query query) {
        Page<Notice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
 //               .like(StringUtils.isNotBlank(queryParam.getKeywords()), Notice::getName, queryParam.getKeywords())
                .orderByAsc(Notice::getSort);
        return page(page, queryWrapper);
    }
}
