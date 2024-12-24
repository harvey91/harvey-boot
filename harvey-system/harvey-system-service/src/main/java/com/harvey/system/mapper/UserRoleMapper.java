package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统用户角色关联表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-07
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

}
