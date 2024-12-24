package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectRoleByUserId(Long userId);

    List<String> selectRoleCodeByUserId(Long userId);
}
