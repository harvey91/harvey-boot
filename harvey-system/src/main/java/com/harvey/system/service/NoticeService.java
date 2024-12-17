package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.enums.PublishStatusEnum;
import com.harvey.system.exception.BadParameterException;
import com.harvey.system.mapper.NoticeMapper;
import com.harvey.system.mapstruct.NoticeConverter;
import com.harvey.system.model.dto.NoticeDto;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.model.entity.NoticeUser;
import com.harvey.system.model.query.NoticeQuery;
import com.harvey.system.model.vo.NoticeVO;
import com.harvey.system.security.SecurityUtil;
import com.harvey.system.utils.AssertUtil;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
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
    private final UserService userService;

    public Page<Notice> queryPage(NoticeQuery query) {
        Page<Notice> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<Notice>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Notice::getTitle, query.getKeywords())
                .orderByAsc(Notice::getSort);
        return page(page, queryWrapper);
    }

    /**
     * 新增通知
     *
     * @param dto
     */
    @Transactional(rollbackFor = Throwable.class)
    public void saveNotice(NoticeDto dto) {
        Notice entity = converter.toEntity(dto);
        entity.setStatus(PublishStatusEnum.UNPUBLISHED.getValue());
        save(entity);
        // 发送给目标用户
        noticeUserService.saveNoticeUserBatch(entity.getId(), dto.getTargetUserIds(), dto.getTargetType());
    }

    /**
     * 更新通知
     *
     * @param dto
     */
    @Transactional(rollbackFor = Throwable.class)
    public void updateNotice(NoticeDto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        Notice entity = converter.toEntity(dto);
        entity.setStatus(PublishStatusEnum.UNPUBLISHED.getValue());
        updateById(entity);
        // 删除旧的目标用户
        LambdaQueryWrapper<NoticeUser> queryWrapper = new LambdaQueryWrapper<NoticeUser>().eq(NoticeUser::getNoticeId, dto.getId());
        noticeUserService.remove(queryWrapper);
        // 重新发送给目标用户
        noticeUserService.saveNoticeUserBatch(entity.getId(), dto.getTargetUserIds(), dto.getTargetType());
    }

    /**
     * 删除通知
     *
     * @param ids
     */
    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }

    /**
     * 发布通知
     *
     * @param id
     */
    @Transactional(rollbackFor = Throwable.class)
    public void publish(Long id) {
        Notice notice = getNoticeById(id);
        AssertUtil.isTrue(PublishStatusEnum.PUBLISHED.getValue() == notice.getStatus(), "该通知已经发布，无需再发布");

        notice.setStatus(PublishStatusEnum.PUBLISHED.getValue());
        notice.setPublisherId(SecurityUtil.getUserId());
        notice.setPublishTime(LocalDateTime.now());
        updateById(notice);
    }

    /**
     * 撤回通知
     *
     * @param id
     */
    @Transactional(rollbackFor = Throwable.class)
    public void revoke(Long id) {
        Notice notice = getNoticeById(id);
        AssertUtil.isTrue(PublishStatusEnum.PUBLISHED.getValue() != notice.getStatus(), "该通知还未发布，无需撤回");

        notice.setStatus(PublishStatusEnum.REVOKED.getValue());
        notice.setRevokeTime(LocalDateTime.now());
        updateById(notice);
    }

    /**
     * 获取单条通知(不包含通知内容)
     *
     * @param id
     * @return
     */
    public Notice getNoticeById(Long id) {
        LambdaQueryWrapper<Notice> wrapper = new LambdaQueryWrapper<Notice>()
                // 过滤通知内容text字段
                .select(Notice::getId, Notice::getTitle, Notice::getStatus, Notice::getLevel, Notice::getType,
                        Notice::getTargetType, Notice::getCreateTime, Notice::getPublishTime, Notice::getRevokeTime)
                .eq(Notice::getId, id);
        Notice notice = mapper.selectOne(wrapper);
        AssertUtil.isEmpty(notice, "通知不存在");

        return notice;
    }

    public NoticeVO detail(Long id) {
        // 已读
        noticeUserService.read(id);
        Notice notice = getById(id);
        NoticeVO noticeVO = converter.toVO(notice);
        String nickname = userService.findNickname(notice.getPublisherId());
        noticeVO.setPublisherName(nickname);
        return noticeVO;
    }
}
