package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.entity.SysOnlineUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.system.security.LoginUserVO;

/**
 * <p>
 * 系统在线用户表 服务类
 * </p>
 *
 * @author harvey
 * @since 2024-11-14
 */
public interface ISysOnlineUserService extends IService<SysOnlineUser> {

    Page<SysOnlineUser> page(int pageNum, int pageSize);

    void saveByLoginUser(LoginUserVO loginUserVO);

    void updateExpireTime(LoginUserVO loginUserVO);

    void offline(String uuid);
}
