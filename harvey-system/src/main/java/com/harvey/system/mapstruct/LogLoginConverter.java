package com.harvey.system.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.system.model.dto.LogLoginDto;
import com.harvey.system.model.entity.LogLogin;
import com.harvey.system.model.vo.LogLoginVO;
import org.mapstruct.Mapper;

/**
* 登陆日志表 转换类
*
* @author harvey
* @since 2024-11-21
*/
@Mapper(componentModel = "spring")
public interface LogLoginConverter extends IConverter<LogLogin, LogLoginDto, LogLoginVO> {

}
