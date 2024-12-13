package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.DictDto;
import com.harvey.system.model.entity.Dict;
import com.harvey.system.model.vo.DictVO;
import org.mapstruct.Mapper;

/**
* 字典管理 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface DictConverter extends IConverter<Dict, DictDto, DictVO> {

}
