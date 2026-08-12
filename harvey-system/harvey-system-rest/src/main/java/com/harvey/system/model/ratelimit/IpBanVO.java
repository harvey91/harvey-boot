package com.harvey.system.model.ratelimit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * <p>
 * 封禁IP信息
 * </p>
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@Schema(title = "封禁IP对象", description = "被封禁的IP信息")
public class IpBanVO {

    @Schema(description = "IP地址")
    private String ip;

    @Schema(description = "封禁时间")
    private LocalDateTime banTime;

    @Schema(description = "解封时间")
    private LocalDateTime expireTime;

    @Schema(description = "剩余封禁时长(秒)")
    private Long remainingSeconds;
}
