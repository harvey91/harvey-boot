package com.harvey.system.model.db;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 数据库列信息
 *
 * @author harvey
 * @since 2026-08-10
 */
@Data
@Schema(title = "ColumnInfoVO", description = "数据库列信息")
public class ColumnInfoVO {

    /** 列名 */
    @Schema(title = "columnName", description = "列名")
    private String columnName;

    /** 列类型(含长度) */
    @Schema(title = "columnType", description = "列类型(含长度)")
    private String columnType;

    /** 数据类型 */
    @Schema(title = "dataType", description = "数据类型")
    private String dataType;

    /** 最大长度 */
    @Schema(title = "maxLength", description = "最大长度")
    private Long maxLength;

    /** 是否可空 */
    @Schema(title = "isNullable", description = "是否可空")
    private String isNullable;

    /** 默认值 */
    @Schema(title = "columnDefault", description = "默认值")
    private String columnDefault;

    /** 列注释 */
    @Schema(title = "columnComment", description = "列注释")
    private String columnComment;

    /** 列键(PRI/UNI/MUL) */
    @Schema(title = "columnKey", description = "列键(PRI/UNI/MUL)")
    private String columnKey;

    /** 扩展属性(auto_increment) */
    @Schema(title = "extra", description = "扩展属性(auto_increment)")
    private String extra;

    /** 位置 */
    @Schema(title = "ordinalPosition", description = "位置")
    private Integer ordinalPosition;

    /** 是否主键 */
    public boolean isPk() {
        return "PRI".equalsIgnoreCase(columnKey);
    }

}
