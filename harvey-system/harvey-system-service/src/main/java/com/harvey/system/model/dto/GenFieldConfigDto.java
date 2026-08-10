package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 代码生成字段配置
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "GenFieldConfigDto", description = "代码生成字段配置")
public class GenFieldConfigDto {

    @Schema(title = "主键")
    private Long id;

    @Schema(title = "列名")
    private String columnName;

    @Schema(title = "列类型")
    private String columnType;

    @Schema(title = "字段名")
    private String fieldName;

    @Schema(title = "字段类型")
    private String fieldType;

    @Schema(title = "字段注释")
    private String fieldComment;

    @Schema(title = "是否列表显示：0否，1是")
    private Integer isShowInList;

    @Schema(title = "是否表单显示：0否，1是")
    private Integer isShowInForm;

    @Schema(title = "是否查询条件：0否，1是")
    private Integer isShowInQuery;

    @Schema(title = "是否必填：0否，1是")
    private Integer isRequired;

    @Schema(title = "表单类型")
    private Integer formType;

    @Schema(title = "查询类型")
    private Integer queryType;

    @Schema(title = "最大长度")
    private Integer maxLength;

    @Schema(title = "字段排序")
    private Integer fieldSort;

    @Schema(title = "字典类型")
    private String dictType;

}
