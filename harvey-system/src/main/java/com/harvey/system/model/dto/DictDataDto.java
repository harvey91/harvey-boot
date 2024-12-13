package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 字典数据
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@Schema(title = "DictDataDto")
public class DictDataDto {
    private Long id;

    @NotBlank(message = "字典编码不能为空")
    @Schema(title = "dictCode", description = "字典编码")
    private String dictCode;

    @NotBlank(message = "字典项不能为空")
    @Schema(title = "label", description = "字典项")
    private String label;

    @NotBlank(message = "字典值不能为空")
    @Schema(title = "value", description = "字典值")
    private String value;

    @Schema(title = "tag", description = "前端标签tag值")
    private String tag;

}
