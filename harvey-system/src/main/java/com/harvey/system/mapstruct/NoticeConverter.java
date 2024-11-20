package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.NoticeDto;
import com.harvey.system.model.entity.Notice;
import com.harvey.system.model.vo.NoticeVO;
import org.mapstruct.Mapper;

/**
* 系统通知表 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface NoticeConverter extends IConverter<Notice, NoticeDto, NoticeVO> {

}
