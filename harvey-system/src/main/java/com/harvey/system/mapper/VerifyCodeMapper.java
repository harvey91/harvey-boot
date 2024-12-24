package com.harvey.system.mapper;

import com.harvey.system.model.entity.VerifyCode;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 验证码 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2024-12-11
 */
@Mapper
public interface VerifyCodeMapper extends BaseMapper<VerifyCode> {

}
