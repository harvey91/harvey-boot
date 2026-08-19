package com.harvey.screen.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.screen.model.entity.ScreenCommand;
import org.apache.ibatis.annotations.Mapper;

/**
 * 信发指令记录 Mapper
 *
 * @author Harvey
 */
@Mapper
public interface ScreenCommandMapper extends BaseMapper<ScreenCommand> {
}
