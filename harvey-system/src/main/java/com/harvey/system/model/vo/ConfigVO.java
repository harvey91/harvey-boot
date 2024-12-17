package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * 系统配置
 * </p>
 *
 * @author harvey
 * @since 2024-11-13
 */
@Data
@Schema(title = "ConfigVO")
public class ConfigVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank(message = "配置名称不能为空")
    @Schema(title = "configName", description = "配置名称")
    private String configName;

    @NotBlank(message = "配置键key不能为空")
    @Schema(title = "configKey", description = "配置键key")
    private String configKey;

    @NotBlank(message = "配置值value不能为空")
    @Schema(title = "configValue", description = "配置值value")
    private String configValue;

}
