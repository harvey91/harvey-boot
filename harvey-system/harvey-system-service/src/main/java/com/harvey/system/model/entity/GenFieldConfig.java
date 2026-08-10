package com.harvey.system.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.core.model.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 代码生成字段配置
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_gen_field_config")
@Schema(title = "GenFieldConfig对象", description = "代码生成字段配置")
public class GenFieldConfig extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "configId", description = "生成配置id")
    private Long configId;

    @Schema(title = "columnName", description = "列名")
    private String columnName;

    @Schema(title = "columnType", description = "列类型")
    private String columnType;

    @Schema(title = "fieldName", description = "字段名")
    private String fieldName;

    @Schema(title = "fieldType", description = "字段类型")
    private String fieldType;

    @Schema(title = "fieldComment", description = "字段注释")
    private String fieldComment;

    @Schema(title = "isShowInList", description = "是否列表显示：0否，1是")
    private Integer isShowInList;

    @Schema(title = "isShowInForm", description = "是否表单显示：0否，1是")
    private Integer isShowInForm;

    @Schema(title = "isShowInQuery", description = "是否查询条件：0否，1是")
    private Integer isShowInQuery;

    @Schema(title = "isRequired", description = "是否必填：0否，1是")
    private Integer isRequired;

    @Schema(title = "formType", description = "表单类型")
    private Integer formType;

    @Schema(title = "queryType", description = "查询类型")
    private Integer queryType;

    @Schema(title = "maxLength", description = "最大长度")
    private Integer maxLength;

    @Schema(title = "fieldSort", description = "字段排序")
    private Integer fieldSort;

    @Schema(title = "dictType", description = "字典类型")
    private String dictType;

}
