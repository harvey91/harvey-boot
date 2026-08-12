package com.harvey.system.model.cache;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 缓存 key 列表对象
 *
 * @author harvey
 * @since 2026-08-12
 */
@Data
@Schema(title = "CacheKeyVO", description = "缓存 key 列表对象")
public class CacheKeyVO {

    /** key */
    @Schema(title = "key", description = "缓存 key")
    private String key;

    /** 数据类型：STRING/HASH/LIST/SET/ZSET */
    @Schema(title = "type", description = "数据类型")
    private String type;

    /** 剩余过期时间(秒)，-1 表示永不过期 */
    @Schema(title = "ttl", description = "剩余过期时间(秒)，-1 表示永不过期")
    private Long ttl;

    /** 数据大小 */
    @Schema(title = "size", description = "数据大小")
    private Long size;

    /** 值预览 */
    @Schema(title = "valuePreview", description = "值预览")
    private String valuePreview;
}
