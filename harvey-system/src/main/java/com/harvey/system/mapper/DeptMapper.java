package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.Dept;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统部门表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-10-29
 */
@Mapper
public interface DeptMapper extends BaseMapper<Dept> {

}
