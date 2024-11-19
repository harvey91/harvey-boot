package ${package.ServiceImpl};

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.system.domain.query.Query;
import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
<#if generateService>
import ${package.Service}.${table.serviceName};
</#if>
import ${superServiceImplClassPackage};
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

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

    public Page<${entity}> queryPage(Query query) {
        Page<${entity}> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<${entity}> queryWrapper = new LambdaQueryWrapper<${entity}>()
 //               .like(StringUtils.isNotBlank(queryParam.getKeywords()), ${entity}::getName, queryParam.getKeywords())
                .orderByAsc(${entity}::getSort);
        return page(page, queryWrapper);
    }
}
