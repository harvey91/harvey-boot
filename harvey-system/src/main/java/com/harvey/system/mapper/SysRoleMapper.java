package com.harvey.system.mapper;

import com.harvey.system.entity.SysRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 系统角色表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-10-28
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {

    List<SysRole> selectRoleByUserId(Long userId);
}
