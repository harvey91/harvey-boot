package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.entity.SysUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Harvey
 * @date 2024-10-24 21:26
 **/
public interface SysUserMapper extends BaseMapper<SysUser> {
    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息信息
     */
    @Select("SELECT * FROM sys_user WHERE `username` = #{username} LIMIT 1")
    SysUser selectByUsername(@Param("username") String username);

}
