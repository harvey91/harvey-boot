package com.harvey.system.model.cache;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 新增/修改缓存对象
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@Schema(title = "CacheValueDTO", description = "新增/修改缓存对象")
public class CacheValueDTO {

    /** key */
    @Schema(title = "key", description = "缓存 key")
    @NotBlank(message = "缓存 key 不能为空")
    private String key;

    /** 值(字符串或JSON) */
    @Schema(title = "value", description = "值(字符串或JSON)")
    private String value;

    /** 过期时间(秒)，为空或小于等于0表示永不过期 */
    @Schema(title = "ttl", description = "过期时间(秒)，为空或小于等于0表示永不过期")
    private Long ttl;
}
