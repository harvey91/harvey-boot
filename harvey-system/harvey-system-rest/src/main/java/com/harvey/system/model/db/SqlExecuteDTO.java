package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 在线 SQL 执行参数
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "SqlExecuteDTO", description = "在线 SQL 执行参数")
public class SqlExecuteDTO {

    @Schema(title = "数据库名")
    private String database;

    @Schema(title = "SQL 语句")
    @NotBlank(message = "SQL 语句不能为空")
    private String sql;

}
