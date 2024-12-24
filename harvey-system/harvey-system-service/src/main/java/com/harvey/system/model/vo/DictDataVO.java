package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 字典数据
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@Schema(title = "DictDataVO")
public class DictDataVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @Schema(title = "dictCode", description = "字典编码")
    private String dictCode;

    @Schema(title = "label", description = "字典项")
    private String label;

    @Schema(title = "value", description = "字典值")
    private String value;

    @Schema(title = "tag", description = "前端标签tag值")
    private String tag;

}
