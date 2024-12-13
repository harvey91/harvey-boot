package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.DictDataDto;
import com.harvey.system.model.entity.DictData;
import com.harvey.system.model.vo.DictDataVO;
import org.mapstruct.Mapper;

/**
* 字典数据 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface DictDataConverter extends IConverter<DictData, DictDataDto, DictDataVO> {

}
