package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 表数据分页结果
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "TableDataVO", description = "表数据分页结果")
public class TableDataVO {

    /** 列信息 */
    @Schema(title = "columns", description = "列信息")
    private List<ColumnInfoVO> columns;

    /** 数据行 */
    @Schema(title = "rows", description = "数据行")
    private List<Map<String, Object>> rows;

    /** 主键列名 */
    @Schema(title = "primaryKey", description = "主键列名")
    private String primaryKey;

    /** 当前页 */
    @Schema(title = "current", description = "当前页")
    private long current;

    /** 每页大小 */
    @Schema(title = "size", description = "每页大小")
    private long size;

    /** 总记录数 */
    @Schema(title = "total", description = "总记录数")
    private long total;

}
