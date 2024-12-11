package com.harvey.system.mapstruct;

import com.harvey.system.model.dto.VerifyCodeDto;
import com.harvey.system.model.entity.VerifyCode;
import com.harvey.system.model.vo.VerifyCodeVO;
import org.mapstruct.Mapper;

/**
* 验证码 转换类
*
* @author harvey
* @since 2024-12-11
*/
@Mapper(componentModel = "spring")
public interface VerifyCodeConverter extends IConverter<VerifyCode, VerifyCodeDto, VerifyCodeVO> {

}
