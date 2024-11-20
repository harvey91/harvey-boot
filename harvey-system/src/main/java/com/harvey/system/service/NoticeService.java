package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.enums.NoticeTargetTypeEnum;
import com.harvey.system.enums.PublishStatusEnum;
import com.harvey.system.exception.BadParameterException;
import com.harvey.system.exception.BusinessException;
import com.harvey.system.mapper.NoticeMapper;
import com.harvey.system.mapstruct.NoticeConverter;
import com.harvey.system.model.dto.NoticeDto;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.model.entity.NoticeUser;
import com.harvey.system.model.query.NoticeQuery;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final NoticeUserService noticeUserService;

    public Page<Notice> queryPage(NoticeQuery query) {
        Page<Notice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Notice::getTitle, query.getKeywords())
                .orderByAsc(Notice::getSort);
        return page(page, queryWrapper);
    }


    @Transactional(rollbackFor = Throwable.class)
    public void saveNotice(NoticeDto dto) {
        Notice entity = converter.toEntity(dto);
        entity.setStatus(0);
        save(entity);
        if (NoticeTargetTypeEnum.TARGET.getValue() == entity.getTargetType()) {
            // 指定用户
            List<Long> targetUserIds = dto.getTargetUserIds();
            if (ObjectUtils.isEmpty(targetUserIds)) {
                throw new BadParameterException("指定用户不能为空");
            }
            List<NoticeUser> noticeUserList = new ArrayList<>();
            for (Long userId : targetUserIds) {
                noticeUserList.add(NoticeUser.builder().noticeId(entity.getId()).userId(userId).build());
            }
            noticeUserService.saveBatch(noticeUserList);
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateNotice(NoticeDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        LambdaQueryWrapper<NoticeUser> queryWrapper = new LambdaQueryWrapper<NoticeUser>().eq(NoticeUser::getNoticeId, dto.getId());
        long count = noticeUserService.count(queryWrapper);
        if (count > 0) {
            // 有指定用户

        }
        Notice entity = converter.toEntity(dto);
        entity.setStatus(0);
        updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void publish(Long id) {
        Notice notice = getNoticeById(id);
        if (PublishStatusEnum.PUBLISHED.getValue() == notice.getStatus()) {
            throw new BusinessException("该通知已经发布，无需再发布");
        }
        notice.setStatus(PublishStatusEnum.PUBLISHED.getValue());
        notice.setPublisherId(SecurityUtil.getUserId());
        notice.setPublishTime(LocalDateTime.now());
        updateById(notice);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void revoke(Long id) {
        Notice notice = getNoticeById(id);
        if (PublishStatusEnum.PUBLISHED.getValue() != notice.getStatus()) {
            throw new BusinessException("该通知还未发布，无需撤回");
        }
        notice.setStatus(PublishStatusEnum.REVOKED.getValue());
        notice.setRevokeTime(LocalDateTime.now());
        updateById(notice);
    }

    /**
     * 全部已读
     */
    @Transactional(rollbackFor = Throwable.class)
    public void readAll() {
        Long userId = SecurityUtil.getUserId();
        // 查询所有全体类型和指定当前用户的未读通知

    }

    public Notice getNoticeById(Long id) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<Notice>()
                // 过滤通知内容text字段
                .select(Notice::getId, Notice::getTitle, Notice::getStatus, Notice::getLevel, Notice::getType,
                        Notice::getTargetType, Notice::getCreateTime, Notice::getPublishTime, Notice::getRevokeTime)
                .eq(Notice::getId, id);
        Notice notice = mapper.selectOne(wrapper);
        if (ObjectUtils.isEmpty(notice)) {
            throw new BusinessException("通知不存在");
        }
        return notice;
    }
}
