package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.Menu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Mapper
public interface MenuMapper extends BaseMapper<Menu> {

    List<Menu> selectMenuByUserId(Long userId);
}
