package com.harvey.system.model.ratelimit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * <p>
 * 限流配置
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@Schema(title = "限流配置", description = "接口限流参数配置")
public class RateLimitConfig {

    @Schema(description = "是否启用限流")
    private Boolean enabled;

    @NotNull(message = "时间窗口不能为空")
    @Min(value = 1, message = "时间窗口必须大于0")
    @Schema(description = "时间窗口(秒)")
    private Integer windowSeconds;

    @NotNull(message = "最大请求次数不能为空")
    @Min(value = 1, message = "最大请求次数必须大于0")
    @Schema(description = "时间窗口内最大请求次数")
    private Integer maxCount;

    @NotNull(message = "封禁时长不能为空")
    @Min(value = 1, message = "封禁时长必须大于0")
    @Schema(description = "超限后封禁时长(秒)")
    private Integer banSeconds;
}
