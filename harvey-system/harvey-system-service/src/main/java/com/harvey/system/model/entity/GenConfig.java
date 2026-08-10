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
 * 代码生成配置
 * </p>
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_gen_config")
@Schema(title = "GenConfig对象", description = "代码生成配置")
public class GenConfig extends BaseEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(title = "tableName", description = "表名")
    private String tableName;

    @Schema(title = "businessName", description = "业务名")
    private String businessName;

    @Schema(title = "moduleName", description = "模块名")
    private String moduleName;

    @Schema(title = "packageName", description = "包名")
    private String packageName;

    @Schema(title = "entityName", description = "实体名")
    private String entityName;

    @Schema(title = "author", description = "作者")
    private String author;

    @Schema(title = "parentMenuId", description = "上级菜单id")
    private Long parentMenuId;

    @Schema(title = "backendAppName", description = "后端应用名")
    private String backendAppName;

    @Schema(title = "frontendAppName", description = "前端应用名")
    private String frontendAppName;

}
