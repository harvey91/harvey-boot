package com.harvey.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.system.model.entity.Dict;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 系统字典表 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Mapper
public interface DictMapper extends BaseMapper<Dict> {

}
