package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.Config;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统配置表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Mapper
public interface ConfigMapper extends BaseMapper<Config> {

}
