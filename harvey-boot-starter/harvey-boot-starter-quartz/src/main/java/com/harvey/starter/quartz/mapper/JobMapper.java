package com.harvey.starter.quartz.mapper;

import com.harvey.starter.quartz.model.entity.Job;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 定时任务调度 Mapper 接口
 * </p>
 *
 * @author harvey
 * @since 2025-04-08
 */
@Mapper
public interface JobMapper extends BaseMapper<Job> {

}
