package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.model.query.NoticeUserQuery;
import com.harvey.system.model.entity.NoticeUser;
import com.harvey.system.mapper.NoticeUserMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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

    public Page<NoticeUser> queryPage(NoticeUserQuery query) {
        Page<NoticeUser> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<NoticeUser> queryWrapper = new LambdaQueryWrapper<NoticeUser>()
//               .like(StringUtils.isNotBlank(queryParam.getKeywords()), NoticeUser::getName, queryParam.getKeywords())
//                .orderByAsc(NoticeUser::getSort)
                ;
        return page(page, queryWrapper);
    }
}
