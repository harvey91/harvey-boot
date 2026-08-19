package com.harvey.screen.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.screen.model.dto.ScreenDeviceGroupDto;
import com.harvey.screen.model.entity.ScreenDeviceGroup;
import com.harvey.screen.model.vo.ScreenDeviceGroupVO;
import org.mapstruct.Mapper;

/**
 * 信发设备分组 转换类
 *
 * @author Harvey
 */
@Mapper(componentModel = "spring")
public interface ScreenDeviceGroupConverter extends IConverter<ScreenDeviceGroup, ScreenDeviceGroup, ScreenDeviceGroupVO> {

    ScreenDeviceGroup toEntity(ScreenDeviceGroupDto dto);
}