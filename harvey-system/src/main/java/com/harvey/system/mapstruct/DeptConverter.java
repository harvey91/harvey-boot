package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.DeptDto;
import com.harvey.system.model.entity.Dept;
import com.harvey.system.model.vo.DeptVO;
import org.mapstruct.Mapper;

/**
* 部门管理 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface DeptConverter extends IConverter<Dept, DeptDto, DeptVO> {

}
