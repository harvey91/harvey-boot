package com.harvey.system.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.system.model.dto.MenuDto;
import com.harvey.system.model.entity.Menu;
import com.harvey.system.model.vo.MenuVO;
import org.mapstruct.Mapper;

/**
* 菜单管理 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface MenuConverter extends IConverter<Menu, MenuDto, MenuVO> {

}
