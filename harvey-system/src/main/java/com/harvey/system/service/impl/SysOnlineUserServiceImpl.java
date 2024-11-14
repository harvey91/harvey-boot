package com.harvey.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.constant.Constant;
import com.harvey.system.entity.SysDict;
import com.harvey.system.entity.SysOnlineUser;
import com.harvey.system.mapper.SysOnlineUserMapper;
import com.harvey.system.security.LoginUserVO;
import com.harvey.system.service.ISysOnlineUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class SysOnlineUserServiceImpl extends ServiceImpl<SysOnlineUserMapper, SysOnlineUser> implements ISysOnlineUserService {

    @Override
    public Page<SysOnlineUser> page(int pageNum, int pageSize) {
        Page<SysOnlineUser> page = new Page<>(pageNum, pageSize);
        LambdaQueryWrapper<SysOnlineUser> queryWrapper = new LambdaQueryWrapper<SysOnlineUser>()
                .orderByDesc(SysOnlineUser::getStatus)
                .orderByDesc(SysOnlineUser::getCreateTime);
        Page<SysOnlineUser> onlineUserPage = page(page, queryWrapper);
        List<SysOnlineUser> onlineUserList = onlineUserPage.getRecords();
        if (!ObjectUtils.isEmpty(onlineUserList)) {
            for (SysOnlineUser onlineUser : onlineUserList) {
                if (LocalDateTime.now().isAfter(onlineUser.getExpireTime())) {
                    // 当前时间晚于过期时间，表示已下线
                    onlineUser.setStatus(Constant.OFFLINE_STATUS);
                }
            }
        }
        return onlineUserPage;
    }

    @Override
    public void saveByLoginUser(LoginUserVO loginUserVO) {
        SysOnlineUser onlineUser = new SysOnlineUser();
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

    @Override
    public void updateExpireTime(LoginUserVO loginUserVO) {
        SysOnlineUser onlineUser = getById(loginUserVO.getUuid());
        if (ObjectUtils.isEmpty(onlineUser)) {
            saveByLoginUser(loginUserVO);
        } else {
            LocalDateTime expireTime = Instant.ofEpochMilli(loginUserVO.getExpireTime()).atZone(ZoneOffset.ofHours(8)).toLocalDateTime();
            onlineUser.setExpireTime(expireTime);
            onlineUser.setStatus(Constant.ONLINE_STATUS);
            updateById(onlineUser);
        }
    }

    @Override
    public void offline(String uuid) {
        SysOnlineUser onlineUser = getById(uuid);
        if (!ObjectUtils.isEmpty(onlineUser)) {
            onlineUser.setStatus(Constant.OFFLINE_STATUS);
            updateById(onlineUser);
        }
    }
}
