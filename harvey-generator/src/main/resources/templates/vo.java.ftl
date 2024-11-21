package ${package.Parent}.model.vo;

<#list table.importPackages as pkg>
import ${pkg};
</#list>
import java.io.Serial;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
/**
* ${table.comment!} VO类
*
* @author ${author}
* @since ${date}
*/
@Data
public class ${entity}VO {

    private Long id;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    <#if field.comment!?length gt 0>
    @Schema(description = "${field.comment}")
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->
}
