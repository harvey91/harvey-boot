package com.harvey.quartz.mapstruct;

import com.harvey.core.mapstruct.IConverter;
import com.harvey.quartz.model.dto.JobLogDto;
import com.harvey.quartz.model.entity.JobLog;
import com.harvey.quartz.model.vo.vo.JobLogVO;
import org.mapstruct.Mapper;

/**
* 定时任务调度日志 转换类
*
* @author harvey
* @since 2025-04-08
*/
@Mapper(componentModel = "spring")
public interface JobLogConverter extends IConverter<JobLog, JobLogDto, JobLogVO> {

}
