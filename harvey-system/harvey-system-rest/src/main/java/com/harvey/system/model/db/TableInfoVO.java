package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据库表信息
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "TableInfoVO", description = "数据库表信息")
public class TableInfoVO {

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

    /** 数据量 */
    @Schema(title = "tableRows", description = "数据量")
    private Long tableRows;

    /** 数据大小(字节) */
    @Schema(title = "dataLength", description = "数据大小(字节)")
    private Long dataLength;

    /** 创建时间 */
    @Schema(title = "createTime", description = "创建时间")
    private String createTime;

    /** 更新时间 */
    @Schema(title = "updateTime", description = "更新时间")
    private String updateTime;

}
