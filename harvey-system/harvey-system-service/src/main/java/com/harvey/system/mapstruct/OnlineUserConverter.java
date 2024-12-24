package com.harvey.system.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.system.model.dto.OnlineUserDto;
import com.harvey.system.model.entity.OnlineUser;
import com.harvey.system.model.vo.OnlineUserVO;
import org.mapstruct.Mapper;

/**
* 系统配置 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface OnlineUserConverter extends IConverter<OnlineUser, OnlineUserDto, OnlineUserVO> {

}
