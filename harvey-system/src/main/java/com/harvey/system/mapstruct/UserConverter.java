package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.UserDto;
import com.harvey.system.model.entity.User;
import com.harvey.system.model.vo.UserVO;
import org.mapstruct.Mapper;

/**
 * @author Harvey
 * @date 2024-11-07 11:41
 **/
@Mapper(componentModel = "spring")
public interface UserConverter extends IConverter<User, UserDto, UserVO> {

}
