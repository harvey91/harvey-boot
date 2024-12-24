package com.harvey.storage.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.storage.model.dto.FileManageDto;
import com.harvey.storage.model.entity.FileManage;
import com.harvey.storage.model.vo.FileManageVO;
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
