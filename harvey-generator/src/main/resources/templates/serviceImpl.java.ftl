package ${package.ServiceImpl};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${package.Parent}.model.query.${entity}Query;
import ${package.Parent}.mapstruct.${entity}Converter;
import ${package.Parent}.model.dto.${entity}Dto;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import com.harvey.system.utils.StringUtils;
import com.harvey.system.exception.BadParameterException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import java.util.List;

/**
 * ${table.comment!} 服务实现类
 *
 * @author ${author}
 * @since ${date}
 */
@Service
@RequiredArgsConstructor
<#if generateService>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> {
</#if>
    private final ${table.mapperName} mapper;
    private final ${entity}Converter converter;

    public Page<${entity}> queryPage(${entity}Query query) {
        Page<${entity}> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<${entity}> queryWrapper = new LambdaQueryWrapper<${entity}>()
//                .like(StringUtils.isNotBlank(query.getKeywords()), ${entity}::getName, query.getKeywords())
                .orderByAsc(${entity}::getSort);
        return page(page, queryWrapper);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void save${entity}(${entity}Dto dto) {
        ${entity} entity = converter.toEntity(dto);
        save(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void update${entity}(${entity}Dto dto) {
        if (ObjectUtils.isEmpty(dto.getId())) {
            throw new BadParameterException();
        }
        ${entity} entity = converter.toEntity(dto);
        updateById(entity);
    }

    @Transactional(rollbackFor = Throwable.class)
    public void deleteByIds(List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            throw new BadParameterException();
        }
        removeByIds(ids);
    }
}
