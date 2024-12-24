package com.harvey.system.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 字典管理
 * </p>
 *
 * @author harvey
 * @since 2024-10-31
 */
@Data
@Schema(title = "DictDto")
public class DictDto {

    private Long id;

    @NotBlank(message = "字典编码不能为空")
    @Schema(title = "dictCode",description = "字典编码")
    private String dictCode;

    @NotBlank(message = "字典名称不能为空")
    @Schema(title = "dictName",description = "字典名称")
    private String dictName;

}
