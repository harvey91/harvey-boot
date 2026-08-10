package com.harvey.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.system.mapper.GenConfigMapper;
import com.harvey.system.mapper.GenFieldConfigMapper;
import com.harvey.system.model.dto.GenConfigDto;
import com.harvey.system.model.dto.GenFieldConfigDto;
import com.harvey.system.model.entity.GenConfig;
import com.harvey.system.model.entity.GenFieldConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 代码生成配置 服务实现类
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Service
@RequiredArgsConstructor
public class GenConfigService extends ServiceImpl<GenConfigMapper, GenConfig> {
    private final GenConfigMapper mapper;
    private final GenFieldConfigMapper fieldConfigMapper;

    /**
     * 根据表名获取生成配置（含字段配置），不存在则返回 null
     */
    public GenConfigDto getByTableName(String tableName) {
        GenConfig config = mapper.selectOne(new LambdaQueryWrapper<GenConfig>()
                .eq(GenConfig::getTableName, tableName).last("LIMIT 1"));
        if (config == null) {
            return null;
        }
        GenConfigDto dto = toDto(config);
        List<GenFieldConfig> fieldConfigs = fieldConfigMapper.selectList(new LambdaQueryWrapper<GenFieldConfig>()
                .eq(GenFieldConfig::getConfigId, config.getId())
                .orderByAsc(GenFieldConfig::getFieldSort));
        dto.setFieldConfigs(fieldConfigs.stream().map(this::fieldToDto).collect(Collectors.toList()));
        return dto;
    }

    /**
     * 是否存在生成配置
     */
    public boolean existsByTableName(String tableName) {
        return mapper.selectCount(new LambdaQueryWrapper<GenConfig>()
                .eq(GenConfig::getTableName, tableName)) > 0;
    }

    /**
     * 保存生成配置（存在则更新）
     */
    @Transactional(rollbackFor = Throwable.class)
    public GenConfigDto saveConfig(GenConfigDto dto) {
        GenConfig config = toEntity(dto);
        GenConfig saved;
        if (config.getId() == null) {
            mapper.insert(config);
            saved = config;
        } else {
            mapper.updateById(config);
            saved = mapper.selectById(config.getId());
        }
        // 更新字段配置：先删后插
        fieldConfigMapper.delete(new LambdaQueryWrapper<GenFieldConfig>()
                .eq(GenFieldConfig::getConfigId, saved.getId()));
        if (!ObjectUtils.isEmpty(dto.getFieldConfigs())) {
            int sort = 0;
            for (GenFieldConfigDto fieldDto : dto.getFieldConfigs()) {
                GenFieldConfig fieldConfig = fieldToEntity(fieldDto);
                fieldConfig.setConfigId(saved.getId());
                if (fieldConfig.getFieldSort() == null) {
                    fieldConfig.setFieldSort(sort++);
                }
                fieldConfigMapper.insert(fieldConfig);
            }
        }
        return getByTableName(saved.getTableName());
    }

    /**
     * 重置生成配置
     */
    @Transactional(rollbackFor = Throwable.class)
    public void resetConfig(String tableName) {
        GenConfig config = mapper.selectOne(new LambdaQueryWrapper<GenConfig>()
                .eq(GenConfig::getTableName, tableName).last("LIMIT 1"));
        if (config != null) {
            fieldConfigMapper.delete(new LambdaQueryWrapper<GenFieldConfig>()
                    .eq(GenFieldConfig::getConfigId, config.getId()));
            mapper.deleteById(config.getId());
        }
    }

    private GenConfigDto toDto(GenConfig entity) {
        GenConfigDto dto = new GenConfigDto();
        dto.setId(entity.getId());
        dto.setTableName(entity.getTableName());
        dto.setBusinessName(entity.getBusinessName());
        dto.setModuleName(entity.getModuleName());
        dto.setPackageName(entity.getPackageName());
        dto.setEntityName(entity.getEntityName());
        dto.setAuthor(entity.getAuthor());
        dto.setParentMenuId(entity.getParentMenuId());
        dto.setBackendAppName(entity.getBackendAppName());
        dto.setFrontendAppName(entity.getFrontendAppName());
        return dto;
    }

    private GenConfig toEntity(GenConfigDto dto) {
        GenConfig entity = new GenConfig();
        entity.setId(dto.getId());
        entity.setTableName(dto.getTableName());
        entity.setBusinessName(dto.getBusinessName());
        entity.setModuleName(dto.getModuleName());
        entity.setPackageName(dto.getPackageName());
        entity.setEntityName(dto.getEntityName());
        entity.setAuthor(dto.getAuthor());
        entity.setParentMenuId(dto.getParentMenuId());
        entity.setBackendAppName(dto.getBackendAppName());
        entity.setFrontendAppName(dto.getFrontendAppName());
        return entity;
    }

    private GenFieldConfigDto fieldToDto(GenFieldConfig entity) {
        GenFieldConfigDto dto = new GenFieldConfigDto();
        dto.setId(entity.getId());
        dto.setColumnName(entity.getColumnName());
        dto.setColumnType(entity.getColumnType());
        dto.setFieldName(entity.getFieldName());
        dto.setFieldType(entity.getFieldType());
        dto.setFieldComment(entity.getFieldComment());
        dto.setIsShowInList(entity.getIsShowInList());
        dto.setIsShowInForm(entity.getIsShowInForm());
        dto.setIsShowInQuery(entity.getIsShowInQuery());
        dto.setIsRequired(entity.getIsRequired());
        dto.setFormType(entity.getFormType());
        dto.setQueryType(entity.getQueryType());
        dto.setMaxLength(entity.getMaxLength());
        dto.setFieldSort(entity.getFieldSort());
        dto.setDictType(entity.getDictType());
        return dto;
    }

    private GenFieldConfig fieldToEntity(GenFieldConfigDto dto) {
        GenFieldConfig entity = new GenFieldConfig();
        entity.setId(dto.getId());
        entity.setColumnName(dto.getColumnName());
        entity.setColumnType(dto.getColumnType());
        entity.setFieldName(dto.getFieldName());
        entity.setFieldType(dto.getFieldType());
        entity.setFieldComment(dto.getFieldComment());
        entity.setIsShowInList(dto.getIsShowInList() == null ? 1 : dto.getIsShowInList());
        entity.setIsShowInForm(dto.getIsShowInForm() == null ? 1 : dto.getIsShowInForm());
        entity.setIsShowInQuery(dto.getIsShowInQuery() == null ? 0 : dto.getIsShowInQuery());
        entity.setIsRequired(dto.getIsRequired() == null ? 0 : dto.getIsRequired());
        entity.setFormType(dto.getFormType() == null ? 1 : dto.getFormType());
        entity.setQueryType(dto.getQueryType() == null ? 1 : dto.getQueryType());
        entity.setMaxLength(dto.getMaxLength() == null ? 0 : dto.getMaxLength());
        entity.setFieldSort(dto.getFieldSort() == null ? 0 : dto.getFieldSort());
        entity.setDictType(dto.getDictType());
        return entity;
    }
}
