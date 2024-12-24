package com.harvey.system.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.system.model.dto.PostDto;
import com.harvey.system.model.entity.Post;
import com.harvey.system.model.vo.PostVO;
import org.mapstruct.Mapper;

/**
* 职位管理 转换类
*
* @author harvey
* @since 2024-11-20
*/
@Mapper(componentModel = "spring")
public interface PostConverter extends IConverter<Post, PostDto, PostVO> {

}
