package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.enums.NoticeTargetTypeEnum;
import com.harvey.system.exception.BadParameterException;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.query.NoticeUserQuery;
import com.harvey.system.model.entity.NoticeUser;
import com.harvey.system.mapper.NoticeUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.model.vo.NoticeUserVO;
import com.harvey.system.security.SecurityUtil;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统通知指定用户表 服务实现类
 *
 * @author harvey
 * @since 2024-11-20
 */
@Service
@RequiredArgsConstructor
public class NoticeUserService extends ServiceImpl<NoticeUserMapper, NoticeUser> {
    private final NoticeUserMapper mapper;
    private final UserService userService;

    public Page<NoticeUser> queryPage(NoticeUserQuery query) {
        Page<NoticeUser> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<NoticeUser> queryWrapper = new LambdaQueryWrapper<NoticeUser>()
//               .like(StringUtils.isNotBlank(queryParam.getKeywords()), NoticeUser::getName, queryParam.getKeywords())
//                .orderByAsc(NoticeUser::getSort)
                ;
        return page(page, queryWrapper);
    }

    public Page<NoticeUserVO> myPage(NoticeUserQuery query) {
        query.setUserId(SecurityUtil.getUserId());
        Page<NoticeUserVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return mapper.selectMyPage(page, query);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveNoticeUserBatch(Long noticeId, List<Long> targetUserIdList, int targetType) {
        if (NoticeTargetTypeEnum.TARGET.getValue() == targetType) {
            // 指定用户
            if (ObjectUtils.isEmpty(targetUserIdList)) {
                throw new BadParameterException("指定用户不能为空");
            }
        } else {
            // 全体用户
            targetUserIdList = new ArrayList<>();
            targetUserIdList.addAll(userService.listObjs(new LambdaQueryWrapper<User>().select(User::getId)));
        }
        // 通知发送给全部目标用户
        List<NoticeUser> noticeUserList = new ArrayList<>();
        for (Long targetUserId : targetUserIdList) {
            NoticeUser noticeUser = NoticeUser.builder().noticeId(noticeId).userId(targetUserId).build();
            noticeUserList.add(noticeUser);
        }
        saveBatch(noticeUserList);
    }

    /**
     * 单条已读
     * @param noticeId
     */
    @Transactional(rollbackFor = Throwable.class)
    public void read(Long noticeId) {
        Long userId = SecurityUtil.getUserId();
        LambdaUpdateWrapper<NoticeUser> queryWrapper = new LambdaUpdateWrapper<NoticeUser>()
                .set(NoticeUser::getIsRead, 1)
                .set(NoticeUser::getReadTime, LocalDateTime.now())
                .eq(NoticeUser::getNoticeId, noticeId)
                .eq(NoticeUser::getUserId, userId);
        this.update(queryWrapper);
    }

    /**
     * 全部已读
     */
    @Transactional(rollbackFor = Throwable.class)
    public void readAll() {
        Long userId = SecurityUtil.getUserId();
        LambdaUpdateWrapper<NoticeUser> queryWrapper = new LambdaUpdateWrapper<NoticeUser>()
                .set(NoticeUser::getIsRead, 1)
                .set(NoticeUser::getReadTime, LocalDateTime.now())
                .eq(NoticeUser::getUserId, userId);
        this.update(queryWrapper);
    }
}
