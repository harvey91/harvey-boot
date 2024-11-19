package com.harvey.system.mapstruct;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.MapperConfig;

/**
 * @author Harvey
 * @date 2024-11-07 11:36
 **/
@MapperConfig
public interface IConverter<ENTITY, DTO, VO> {

    DTO toDto(ENTITY entity);

    ENTITY toEntity(DTO dto);

    Page<VO> toPage(Page<ENTITY> page);

    VO toVO(ENTITY entity);
}
