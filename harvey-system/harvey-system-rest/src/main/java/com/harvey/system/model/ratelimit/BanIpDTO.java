package com.harvey.system.model.ratelimit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * <p>
 * 手动封禁IP请求
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@Schema(title = "封禁IP请求", description = "手动封禁IP请求参数")
public class BanIpDTO {

    @NotBlank(message = "IP地址不能为空")
    @Schema(description = "IP地址")
    private String ip;

    @Min(value = 1, message = "封禁时长必须大于0")
    @Schema(description = "封禁时长(秒)，为空时使用当前限流配置中的封禁时长")
    private Integer banSeconds;
}
