package com.harvey.starter.quartz.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.starter.quartz.model.dto.JobDto;
import com.harvey.starter.quartz.model.entity.Job;
import com.harvey.starter.quartz.model.vo.vo.JobVO;
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
