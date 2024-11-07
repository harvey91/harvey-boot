package com.harvey.system.mapstruct;

import com.harvey.system.domain.dto.UserDto;
import com.harvey.system.entity.SysUser;
import org.mapstruct.Mapper;

/**
 * @author Harvey
 * @date 2024-11-07 11:41
 **/
@Mapper(componentModel = "spring")
public interface UserConverter extends IConverter<SysUser, UserDto> {

}
