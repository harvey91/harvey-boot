package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.common.constant.CacheConstant;
import com.harvey.system.mapper.ConfigMapper;
import com.harvey.system.mapstruct.ConfigConverter;
import com.harvey.system.model.dto.ConfigDto;
import com.harvey.system.model.entity.Config;
import com.harvey.system.model.query.ConfigQuery;
import com.harvey.system.redis.RedisCache;
import com.harvey.system.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 系统配置 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Service
@RequiredArgsConstructor
public class ConfigService extends ServiceImpl<ConfigMapper, Config> {
    private final ConfigMapper mapper;
    private final ConfigConverter converter;
    private final RedisCache redisCache;

    public Page<Config> queryPage(ConfigQuery query) {
        Page<Config> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<Config> queryWrapper = new LambdaQueryWrapper<Config>()
                .like(StringUtils.isNotBlank(query.getKeywords()), Config::getConfigName, query.getKeywords())
                .orderByAsc(Config::getSort);
        return this.page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveConfig(ConfigDto dto) {
        Config entity = converter.toEntity(dto);
        this.save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateConfig(ConfigDto dto) {
        Config entity = converter.toEntity(dto);
        this.updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        this.removeByIds(ids);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteById(Long id) {
        this.removeById(id);
    }

    /**
     * 刷新缓存
     */
    public void refresh() {
        String key = CacheConstant.SYS_CONFIG_KEY;
        redisCache.delete(key);
        List<Config> list = this.list();
        if (!ObjectUtils.isEmpty(list)) {
            Map<String, String> map = list.stream().collect(Collectors.toMap(Config::getConfigKey, Config::getConfigValue));
            redisCache.hPutAll(key, map);
        }
    }

    /**
     * 根据键key获取配置值value
     * @param key 配置key
     * @return 配置value
     */
    public Object getConfigValue(String key) {
        if (StringUtils.isNotBlank(key)) {
            return redisCache.hGet(CacheConstant.SYS_CONFIG_KEY, key);
        }
        return null;
    }
}
