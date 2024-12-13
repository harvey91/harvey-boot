package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.ConfigDto;
import com.harvey.system.model.entity.Config;
import com.harvey.system.model.vo.ConfigVO;
import org.mapstruct.Mapper;

/**
* 系统配置 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface ConfigConverter extends IConverter<Config, ConfigDto, ConfigVO> {

}
