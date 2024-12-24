package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统角色菜单关联表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-06
 */
@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

}
