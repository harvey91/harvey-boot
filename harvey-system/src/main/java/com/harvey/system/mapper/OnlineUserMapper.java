package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.OnlineUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统在线用户表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-14
 */
@Mapper
public interface OnlineUserMapper extends BaseMapper<OnlineUser> {

}
