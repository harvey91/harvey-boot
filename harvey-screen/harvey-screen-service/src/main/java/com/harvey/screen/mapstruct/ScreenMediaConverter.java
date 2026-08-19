package com.harvey.screen.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.screen.model.entity.ScreenMedia;
import com.harvey.screen.model.vo.ScreenMediaVO;
import org.mapstruct.Mapper;

/**
 * 信发媒体库 转换类
 *
 * @author Harvey
 */
@Mapper(componentModel = "spring")
public interface ScreenMediaConverter extends IConverter<ScreenMedia, ScreenMedia, ScreenMediaVO> {
}