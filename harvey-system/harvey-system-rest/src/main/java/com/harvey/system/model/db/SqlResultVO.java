package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * SQL 执行结果
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "SqlResultVO", description = "SQL 执行结果")
public class SqlResultVO {

    /** 语句类型 SELECT/INSERT/UPDATE/DELETE/DDL/OTHER */
    @Schema(title = "type", description = "语句类型 SELECT/INSERT/UPDATE/DELETE/DDL/OTHER")
    private String type;

    /** 原始SQL */
    @Schema(title = "sql", description = "原始SQL")
    private String sql;

    /** 是否执行成功 */
    @Schema(title = "success", description = "是否执行成功")
    private boolean success = true;

    /** 返回列名 */
    @Schema(title = "columns", description = "返回列名")
    private List<String> columns;

    /** 返回数据行 */
    @Schema(title = "rows", description = "返回数据行")
    private List<Map<String, Object>> rows;

    /** 影响行数 */
    @Schema(title = "affected", description = "影响行数")
    private int affected;

    /** 耗时(ms) */
    @Schema(title = "elapsed", description = "耗时(ms)")
    private long elapsed;

    /** 提示/错误信息 */
    @Schema(title = "message", description = "提示/错误信息")
    private String message;

}
