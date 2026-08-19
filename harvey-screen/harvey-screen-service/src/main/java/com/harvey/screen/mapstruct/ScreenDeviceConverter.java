package com.harvey.screen.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.screen.model.dto.ScreenDeviceDto;
import com.harvey.screen.model.entity.ScreenDevice;
import com.harvey.screen.model.vo.ScreenDeviceVO;
import org.mapstruct.Mapper;

/**
 * 信发设备 转换类
 *
 * @author Harvey
 */
@Mapper(componentModel = "spring")
public interface ScreenDeviceConverter extends IConverter<ScreenDevice, ScreenDeviceDto, ScreenDeviceVO> {
}
