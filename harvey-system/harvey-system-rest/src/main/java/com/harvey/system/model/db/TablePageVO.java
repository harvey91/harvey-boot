package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 代码生成：数据表分页对象
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "TablePageVO", description = "代码生成：数据表分页对象")
public class TablePageVO {

    /** 表名 */
    @Schema(title = "tableName", description = "表名")
    private String tableName;

    /** 表描述 */
    @Schema(title = "tableComment", description = "表描述")
    private String tableComment;

    /** 存储引擎 */
    @Schema(title = "engine", description = "存储引擎")
    private String engine;

    /** 排序规则 */
    @Schema(title = "tableCollation", description = "排序规则")
    private String tableCollation;

    /** 创建时间 */
    @Schema(title = "createTime", description = "创建时间")
    private String createTime;

    /** 是否已配置代码生成 */
    @Schema(title = "isConfigured", description = "是否已配置代码生成")
    private Integer isConfigured;

}
