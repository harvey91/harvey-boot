package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 代码生成配置
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "GenConfigDto", description = "代码生成配置")
public class GenConfigDto {

    @Schema(title = "主键")
    private Long id;

    @Schema(title = "表名")
    private String tableName;

    @Schema(title = "业务名")
    private String businessName;

    @Schema(title = "模块名")
    private String moduleName;

    @Schema(title = "包名")
    private String packageName;

    @Schema(title = "实体名")
    private String entityName;

    @Schema(title = "作者")
    private String author;

    @Schema(title = "上级菜单id")
    private Long parentMenuId;

    @Schema(title = "后端应用名")
    private String backendAppName;

    @Schema(title = "前端应用名")
    private String frontendAppName;

    @Schema(title = "字段配置列表")
    private List<GenFieldConfigDto> fieldConfigs;

}
