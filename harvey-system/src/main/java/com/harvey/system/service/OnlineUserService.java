package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.constant.Constant;
import com.harvey.system.mapper.OnlineUserMapper;
import com.harvey.system.model.entity.OnlineUser;
import com.harvey.system.security.LoginUserVO;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
public class OnlineUserService extends ServiceImpl<OnlineUserMapper, OnlineUser> {

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

    public void saveByLoginUser(LoginUserVO loginUserVO) {
        OnlineUser onlineUser = new OnlineUser();
        onlineUser.setUuid(loginUserVO.getUuid());
        onlineUser.setUserId(loginUserVO.getUserId());
        onlineUser.setUsername(loginUserVO.getUsername());
        onlineUser.setDeptName("");
        onlineUser.setIp(loginUserVO.getIp());
        onlineUser.setLocation(loginUserVO.getLocation());
        onlineUser.setBrowser(loginUserVO.getBrowser());
        onlineUser.setOs(loginUserVO.getOs());
        LocalDateTime loginTime = Instant.ofEpochMilli(loginUserVO.getLoginTime()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        LocalDateTime expireTime = Instant.ofEpochMilli(loginUserVO.getExpireTime()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
        onlineUser.setCreateTime(loginTime);
        onlineUser.setExpireTime(expireTime);
        onlineUser.setStatus(Constant.ONLINE_STATUS);
        save(onlineUser);
    }

    public void updateExpireTime(LoginUserVO loginUserVO) {
        OnlineUser onlineUser = getById(loginUserVO.getUuid());
        if (ObjectUtils.isEmpty(onlineUser)) {
            saveByLoginUser(loginUserVO);
        } else {
            LocalDateTime expireTime = Instant.ofEpochMilli(loginUserVO.getExpireTime()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            onlineUser.setExpireTime(expireTime);
            onlineUser.setStatus(Constant.ONLINE_STATUS);
            updateById(onlineUser);
        }
    }

    public void offline(String uuid) {
        OnlineUser onlineUser = getById(uuid);
        if (!ObjectUtils.isEmpty(onlineUser)) {
            onlineUser.setStatus(Constant.OFFLINE_STATUS);
            updateById(onlineUser);
        }
    }
}
