package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.query.NoticeQuery;
import com.harvey.system.mapstruct.NoticeConverter;
import com.harvey.system.model.dto.NoticeDto;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.mapper.NoticeMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.utils.StringUtils;
import com.harvey.system.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 系统通知表 服务实现类
 *
 * @author harvey
 * @since 2024-11-20
 */
@Service
@RequiredArgsConstructor
public class NoticeService extends ServiceImpl<NoticeMapper, Notice> {
    private final NoticeMapper mapper;
    private final NoticeConverter converter;

    public Page<Notice> queryPage(NoticeQuery query) {
        Page<Notice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Notice::getTitle, query.getKeywords())
                .orderByAsc(Notice::getSort);
        return page(page, queryWrapper);
    }


    @Transactional
    public void saveNotice(NoticeDto dto) {
        Notice entity = converter.toEntity(dto);
        entity.setStatus(0);
        save(entity);
    }

    @Transactional
    public void updateNotice(NoticeDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Notice entity = converter.toEntity(dto);
        entity.setStatus(0);
        updateById(entity);
    }

    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }
}
