package com.harvey.screen.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.screen.model.entity.ScreenCommand;
import com.harvey.screen.model.vo.ScreenCommandVO;
import org.mapstruct.Mapper;

/**
 * 信发指令记录 转换类
 *
 * @author Harvey
 */
@Mapper(componentModel = "spring")
public interface ScreenCommandConverter extends IConverter<ScreenCommand, ScreenCommand, ScreenCommandVO> {
}
