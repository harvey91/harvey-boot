package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author Harvey
 * @date 2024-10-24 21:26
 **/
public interface UserMapper extends BaseMapper<User> {

    @Select("SELECT count(`username`) FROM sys_user WHERE `username` = #{username}")
    long countByUsername(@Param("username") String username);


    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户信息信息
     */
    @Select("SELECT * FROM sys_user WHERE `username` = #{username} LIMIT 1")
    User selectByUsername(@Param("username") String username);

    /**
     * id查询昵称
     * @param id
     * @return
     */
    @Select("SELECT nickname FROM sys_user WHERE `id` = #{id} LIMIT 1")
    String selectNickname(@Param("id") Long id);

}
