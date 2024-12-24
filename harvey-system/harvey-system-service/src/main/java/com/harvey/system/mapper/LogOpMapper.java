package com.harvey.system.mapper;

import com.harvey.system.model.entity.LogOp;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 操作日志表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-11-21
 */
@Mapper
public interface LogOpMapper extends BaseMapper<LogOp> {

}
