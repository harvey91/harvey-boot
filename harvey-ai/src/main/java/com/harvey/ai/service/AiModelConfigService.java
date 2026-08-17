package com.harvey.ai.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.ai.config.AiKeyCrypt;
import com.harvey.ai.config.ModelConfigChangedEvent;
import com.harvey.ai.mapper.AiModelConfigMapper;
import com.harvey.ai.mapstruct.AiModelConfigConverter;
import com.harvey.ai.model.dto.AiModelConfigDto;
import com.harvey.ai.model.entity.AiModelConfig;
import com.harvey.ai.model.query.AiModelConfigQuery;
import com.harvey.ai.model.vo.AiModelConfigVO;
import com.harvey.common.exception.BusinessException;
import com.harvey.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * AI模型配置 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-17
 */
@Service
@RequiredArgsConstructor
public class AiModelConfigService extends ServiceImpl<AiModelConfigMapper, AiModelConfig> {

    private final AiModelConfigMapper mapper;
    private final AiModelConfigConverter converter;
    private final AiKeyCrypt aiKeyCrypt;
    private final ApplicationEventPublisher eventPublisher;

    public Page<AiModelConfigVO> queryPage(AiModelConfigQuery query) {
        Page<AiModelConfig> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<AiModelConfig> queryWrapper = new LambdaQueryWrapper<AiModelConfig>()
                .like(StringUtils.isNotBlank(query.getKeywords()), AiModelConfig::getModelName, query.getKeywords())
                .eq(query.getEnabled() != null, AiModelConfig::getEnabled, query.getEnabled())
                .orderByDesc(AiModelConfig::getIsDefault)
                .orderByAsc(AiModelConfig::getSort);
        Page<AiModelConfig> result = this.page(page, queryWrapper);
        Page<AiModelConfigVO> voPage = new Page<>(result.getCurrent(), result.getSize(), result.getTotal());
        voPage.setRecords(result.getRecords().stream().map(converter::toVO).toList());
        return voPage;
    }

    /**
     * 实体转 VO
     */
    public AiModelConfigVO convertToVO(AiModelConfig entity) {
        return converter.toVO(entity);
    }

    /**
     * 启用的模型列表(用于对话端下拉选择)
     */
    public List<AiModelConfigVO> listEnabled() {
        List<AiModelConfig> list = this.list(new LambdaQueryWrapper<AiModelConfig>()
                .eq(AiModelConfig::getEnabled, 1)
                .orderByAsc(AiModelConfig::getSort));
        return list.stream().map(converter::toVO).toList();
    }

    /**
     * 解密 API 密钥
     */
    public String decryptApiKey(String cipherText) {
        return aiKeyCrypt.decrypt(cipherText);
    }

    /**
     * 默认模型
     */
    public AiModelConfig getDefaultModel() {
        return this.getOne(new LambdaQueryWrapper<AiModelConfig>()
                .eq(AiModelConfig::getIsDefault, 1)
                .eq(AiModelConfig::getEnabled, 1)
                .last("limit 1"));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void saveConfig(AiModelConfigDto dto) {
        AiModelConfig entity = converter.toEntity(dto);
        entity.setApiKey(aiKeyCrypt.encrypt(dto.getApiKey()));
        if (entity.getIsDefault() != null && entity.getIsDefault() == 1) {
            cancelCurrentDefault();
        }
        this.save(entity);
        eventPublisher.publishEvent(new ModelConfigChangedEvent(entity.getId()));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void updateConfig(AiModelConfigDto dto) {
        if (dto.getId() == null) {
            throw new BusinessException("模型ID不能为空");
        }
        AiModelConfig entity = converter.toEntity(dto);
        // apiKey 留空表示不修改
        if (StringUtils.isNotBlank(dto.getApiKey())) {
            entity.setApiKey(aiKeyCrypt.encrypt(dto.getApiKey()));
        } else {
            entity.setApiKey(null);
        }
        if (entity.getIsDefault() != null && entity.getIsDefault() == 1) {
            cancelCurrentDefault(dto.getId());
        }
        this.updateById(entity);
        eventPublisher.publishEvent(new ModelConfigChangedEvent(entity.getId()));
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        ids.forEach(id -> {
            AiModelConfig config = this.getById(id);
            if (config != null && config.getIsDefault() != null && config.getIsDefault() == 1) {
                throw new BusinessException("默认模型不能删除, 请先切换默认模型");
            }
            this.removeById(id);
            eventPublisher.publishEvent(new ModelConfigChangedEvent(id));
        });
        // 若删除后没有默认模型, 自动指定第一个启用模型为默认
        if (getDefaultModel() == null) {
            AiModelConfig first = this.getOne(new LambdaQueryWrapper<AiModelConfig>()
                    .eq(AiModelConfig::getEnabled, 1)
                    .orderByAsc(AiModelConfig::getSort)
                    .last("limit 1"));
            if (first != null) {
                first.setIsDefault(1);
                this.updateById(first);
                eventPublisher.publishEvent(new ModelConfigChangedEvent(first.getId()));
            }
        }
    }

    @Transactional(rollbackFor = Throwable.class)
    public void setDefault(Long id) {
        AiModelConfig config = this.getById(id);
        if (config == null) {
            throw new BusinessException("模型配置不存在");
        }
        cancelCurrentDefault();
        config.setIsDefault(1);
        this.updateById(config);
        eventPublisher.publishEvent(new ModelConfigChangedEvent(id));
    }

    private void cancelCurrentDefault() {
        cancelCurrentDefault(null);
    }

    private void cancelCurrentDefault(Long excludeId) {
        LambdaQueryWrapper<AiModelConfig> wrapper = new LambdaQueryWrapper<AiModelConfig>()
                .eq(AiModelConfig::getIsDefault, 1);
        if (excludeId != null) {
            wrapper.ne(AiModelConfig::getId, excludeId);
        }
        List<AiModelConfig> currentDefaults = this.list(wrapper);
        currentDefaults.forEach(item -> {
            item.setIsDefault(0);
            this.updateById(item);
            eventPublisher.publishEvent(new ModelConfigChangedEvent(item.getId()));
        });
    }
}