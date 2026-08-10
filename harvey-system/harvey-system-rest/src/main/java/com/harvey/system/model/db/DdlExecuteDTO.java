package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DDL 执行参数
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "DdlExecuteDTO", description = "DDL 执行参数")
public class DdlExecuteDTO {

    @Schema(title = "DDL 语句")
    @NotBlank(message = "DDL 语句不能为空")
    private String ddl;

}
