package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.query.NoticeReadQuery;
import com.harvey.system.model.entity.NoticeRead;
import com.harvey.system.mapper.NoticeReadMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

/**
 * 系统通知已读用户表 服务实现类
 *
 * @author harvey
 * @since 2024-11-20
 */
@Service
@RequiredArgsConstructor
public class NoticeReadService extends ServiceImpl<NoticeReadMapper, NoticeRead> {
    private final NoticeReadMapper mapper;

    public Page<NoticeRead> queryPage(NoticeReadQuery query) {
        Page<NoticeRead> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<NoticeRead> queryWrapper = new LambdaQueryWrapper<NoticeRead>()
 //               .like(StringUtils.isNotBlank(queryParam.getKeywords()), NoticeRead::getName, queryParam.getKeywords())
//                .orderByAsc(NoticeRead::getSort)
                ;
        return page(page, queryWrapper);
    }
}
