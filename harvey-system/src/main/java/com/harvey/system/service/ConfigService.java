package com.harvey.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.ConfigMapper;
import com.harvey.system.model.entity.Config;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 系统配置表 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Service
public class ConfigService extends ServiceImpl<ConfigMapper, Config> {

}
