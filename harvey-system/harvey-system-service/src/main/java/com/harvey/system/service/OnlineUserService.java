package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.constant.Constant;
import com.harvey.system.mapper.OnlineUserMapper;
import com.harvey.system.mapstruct.OnlineUserConverter;
import com.harvey.system.model.dto.OnlineUserDto;
import com.harvey.system.model.entity.OnlineUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 系统在线用户表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-14
 */
@Service
@RequiredArgsConstructor
public class OnlineUserService extends ServiceImpl<OnlineUserMapper, OnlineUser> {
    private final OnlineUserConverter converter;

    public Page<OnlineUser> page(int pageNum, int pageSize) {
        Page<OnlineUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<OnlineUser> queryWrapper = new LambdaQueryWrapper<OnlineUser>()
                .orderByDesc(OnlineUser::getStatus)
                .orderByDesc(OnlineUser::getCreateTime);
        Page<OnlineUser> onlineUserPage = page(page, queryWrapper);
        List<OnlineUser> onlineUserList = onlineUserPage.getRecords();
        if (!ObjectUtils.isEmpty(onlineUserList)) {
            for (OnlineUser onlineUser : onlineUserList) {
                if (LocalDateTime.now().isAfter(onlineUser.getExpireTime())) {
                    // 当前时间晚于过期时间，表示已下线
                    onlineUser.setStatus(Constant.OFFLINE_STATUS);
                }
            }
        }
        return onlineUserPage;
    }

    public void saveByLoginUser(OnlineUserDto dto) {
        OnlineUser entity = converter.toEntity(dto);
        this.save(entity);
    }

    public void updateExpireTime(OnlineUserDto dto) {
        OnlineUser onlineUser = this.getById(dto.getUuid());
        if (ObjectUtils.isEmpty(onlineUser)) {
            saveByLoginUser(dto);
        } else {
            onlineUser.setExpireTime(dto.getExpireTime());
            onlineUser.setStatus(Constant.ONLINE_STATUS);
            this.updateById(onlineUser);
        }
    }

    public void offline(String uuid) {
        OnlineUser onlineUser = getById(uuid);
        if (!ObjectUtils.isEmpty(onlineUser)) {
            onlineUser.setStatus(Constant.OFFLINE_STATUS);
            this.updateById(onlineUser);
        }
    }
}
