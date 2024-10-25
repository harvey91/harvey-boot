package com.harvey.web.mapper;

import com.harvey.web.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-24 21:26
 **/
@Mapper
public interface SysUserMapper {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息信息
     */
    SysUser selectUserByUsername(String username);

    /**
     * 查询用户列表
     * @param user
     * @return 用户列表
     */
    List<SysUser> selectUserList(SysUser user);
}
