package com.harvey.quartz.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.quartz.model.dto.JobDto;
import com.harvey.quartz.model.entity.Job;
import com.harvey.quartz.model.vo.vo.JobVO;
import org.mapstruct.Mapper;

/**
* 定时任务调度 转换类
*
* @author harvey
* @since 2025-04-08
*/
@Mapper(componentModel = "spring")
public interface JobConverter extends IConverter<Job, JobDto, JobVO> {

}
