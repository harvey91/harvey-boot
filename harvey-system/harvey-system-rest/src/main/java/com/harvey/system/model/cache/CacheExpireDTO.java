package com.harvey.system.model.cache;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 设置缓存过期时间对象
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@Schema(title = "CacheExpireDTO", description = "设置缓存过期时间对象")
public class CacheExpireDTO {

    /** key */
    @Schema(title = "key", description = "缓存 key")
    @NotBlank(message = "缓存 key 不能为空")
    private String key;

    /** 过期时间(秒) */
    @Schema(title = "ttl", description = "过期时间(秒)")
    @NotNull(message = "过期时间不能为空")
    private Long ttl;
}
