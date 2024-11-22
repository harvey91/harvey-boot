package ${package.Controller};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import ${package.Parent}.model.dto.${entity}Dto;
import ${package.Parent}.model.query.${entity}Query;
import ${package.Entity}.${entity};
import ${package.ServiceImpl}.${table.serviceImplName};
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${table.comment!} 前端控制器
 *
 * @author ${author}
 * @since ${date}
 */
@Tag(name = "${table.comment!}")
@RestController
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@RequiredArgsConstructor
public class ${table.controllerName} {
    private final ${table.serviceImplName} ${table.serviceImplName?uncap_first};

    @Operation(summary = "id查询表单", description = "根据id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<${entity}> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(${table.serviceImplName?uncap_first}.getById(id));
    }

    @Operation(summary = "分页列表")
    @PreAuthorize("@ex.hasPerm('${schemaName?replace('_', ':')}${table.name?replace('_', ':')}:list')")
    @GetMapping("/page")
    public RespResult<PageResult<${entity}>> page(${entity}Query query) {
        Page<${entity}> page = ${table.serviceImplName?uncap_first}.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PreAuthorize("@ex.hasPerm('${schemaName?replace('_', ':')}${table.name?replace('_', ':')}:create')")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody @Validated ${entity}Dto dto) {
        ${table.serviceImplName?uncap_first}.save${entity}(dto);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PreAuthorize("@ex.hasPerm('${schemaName?replace('_', ':')}${table.name?replace('_', ':')}:modify')")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody @Validated ${entity}Dto dto) {
        ${table.serviceImplName?uncap_first}.update${entity}(dto);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @PreAuthorize("@ex.hasPerm('${schemaName?replace('_', ':')}${table.name?replace('_', ':')}:delete')")
    @DeleteMapping("/delete")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        ${table.serviceImplName?uncap_first}.deleteByIds(ids);
        return RespResult.success();
    }
}
