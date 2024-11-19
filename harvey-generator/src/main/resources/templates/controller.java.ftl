package ${package.Controller};

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import com.harvey.system.domain.query.Query;
import ${package.Entity}.${entity};
import ${package.ServiceImpl}.${table.serviceImplName};
import com.harvey.system.base.PageResult;
import com.harvey.system.base.RespResult;
import lombok.RequiredArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ${table.comment!} 前端控制器
 *
 * @author ${author}
 * @since ${date}
 */
@Tag(name = "${table.comment!} Controller")
@RestController
@RequestMapping("<#if package.ModuleName?? && package.ModuleName != "">/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
@RequiredArgsConstructor
public class ${table.controllerName} {
    private final ${table.serviceImplName} ${table.serviceImplName?uncap_first};

    @Operation(summary = "id查询表单", description = "根据配置id查询对象")
    @GetMapping("/form/{id}")
    public RespResult<${entity}> formById(@PathVariable(value = "id") Long id) {
        return RespResult.success(${table.serviceImplName?uncap_first}.getById(id));
    }

    @Operation(summary = "分页列表")
    @GetMapping("/page")
    public RespResult<PageResult<${entity}>> page(Query query) {
        Page<${entity}> page = ${table.serviceImplName?uncap_first}.queryPage(query);
        return RespResult.success(PageResult.of(page));
    }

    @Operation(summary = "新增")
    @PostMapping("/create")
        public RespResult<String> create(@RequestBody ${entity} entity) {
        ${table.serviceImplName?uncap_first}.save(entity);
        return RespResult.success();
    }

    @Operation(summary = "修改")
    @PutMapping("/modify")
    public RespResult<String> modify(@RequestBody ${entity} entity) {
        ${table.serviceImplName?uncap_first}.updateById(entity);
        return RespResult.success();
    }

    @Operation(summary = "删除")
    @DeleteMapping("/delete/{id}")
    public RespResult<String> delete(@RequestBody List<Long> ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return RespResult.fail("id不能为空");
        }
        ${table.serviceImplName?uncap_first}.removeByIds(ids);
        return RespResult.success();
    }
}
