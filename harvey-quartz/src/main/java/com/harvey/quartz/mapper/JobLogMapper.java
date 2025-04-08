package com.harvey.quartz.mapper;

import com.harvey.quartz.model.entity.JobLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 定时任务调度日志 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2025-04-08
 */
@Mapper
public interface JobLogMapper extends BaseMapper<JobLog> {

}
