package com.harvey.system.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.system.model.dto.RoleDto;
import com.harvey.system.model.entity.Role;
import com.harvey.system.model.vo.RoleVO;
import org.mapstruct.Mapper;

/**
* 角色管理 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface RoleConverter extends IConverter<Role, RoleDto, RoleVO> {

}
