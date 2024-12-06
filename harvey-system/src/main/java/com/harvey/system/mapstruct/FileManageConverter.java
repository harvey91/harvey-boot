package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.FileManageDto;
import com.harvey.system.model.entity.FileManage;
import com.harvey.system.model.vo.FileManageVO;
import org.mapstruct.Mapper;

/**
* 文件管理 转换类
*
* @author harvey
* @since 2024-12-05
*/
@Mapper(componentModel = "spring")
public interface FileManageConverter extends IConverter<FileManage, FileManageDto, FileManageVO> {

}
