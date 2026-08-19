package com.harvey.screen.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.screen.model.dto.ScreenMarqueeDto;
import com.harvey.screen.model.entity.ScreenMarquee;
import com.harvey.screen.model.vo.ScreenMarqueeVO;
import org.mapstruct.Mapper;

/**
 * 信发滚动字幕模板 转换类
 *
 * @author Harvey
 */
@Mapper(componentModel = "spring")
public interface ScreenMarqueeConverter extends IConverter<ScreenMarquee, ScreenMarquee, ScreenMarqueeVO> {

    ScreenMarquee toEntity(ScreenMarqueeDto dto);
}