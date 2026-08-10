package ${packageName}.${moduleName}.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
<#if hasBigDecimal>
import java.math.BigDecimal;
</#if>
<#if hasLocalDate>
import java.time.LocalDate;
</#if>
<#if hasLocalDateTime>
import java.time.LocalDateTime;
</#if>
import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * ${tableComment}
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
@Data
<#if extendsBaseEntity>
@EqualsAndHashCode(callSuper = true)
<#else>
@EqualsAndHashCode(callSuper = false)
</#if>
@TableName("${tableName}")
@Schema(name = "${entityName}", description = "${tableComment}")
<#if extendsBaseEntity>
public class ${entityName} extends BaseEntity implements Serializable {
<#else>
public class ${entityName} implements Serializable {
</#if>
    @Serial
    private static final long serialVersionUID = 1L;

<#list columns as column>
    @Schema(description = "${column.fieldComment}")
    <#if column.isPk>
    @TableId(value = "${column.columnName}", type = IdType.AUTO)
    <#elseif column.columnName != column.fieldName>
    @TableField("${column.columnName}")
    </#if>
    private ${column.fieldType} ${column.fieldName};
</#list>
}
