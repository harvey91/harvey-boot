package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.LogOpDto;
import com.harvey.system.model.entity.LogOp;
import com.harvey.system.model.vo.LogOpVO;
import org.mapstruct.Mapper;

/**
* 操作日志表 转换类
*
* @author harvey
* @since 2024-11-21
*/
@Mapper(componentModel = "spring")
public interface LogOpConverter extends IConverter<LogOp, LogOpDto, LogOpVO> {

}
