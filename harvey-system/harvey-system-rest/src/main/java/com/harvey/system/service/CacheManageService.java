package com.harvey.system.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONException;
import com.harvey.common.exception.BadParameterException;
import com.harvey.common.utils.StringUtils;
import com.harvey.core.model.PageResult;
import com.harvey.starter.redis.service.RedisService;
import com.harvey.system.model.cache.CacheExpireDTO;
import com.harvey.system.model.cache.CacheKeyQuery;
import com.harvey.system.model.cache.CacheKeyVO;
import com.harvey.system.model.cache.CacheValueDTO;
import com.harvey.system.model.cache.CacheValueVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理服务：缓存 key 查询、查看、新增/修改、删除、过期时间管理
 *
 * @author harvey
 * @since 2026-08-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheManageService {

    private final RedisService redisService;
    private final RedisTemplate<String, Object> redisTemplate;

    /** SCAN 扫描时最多收集的 key 数量，避免内存占用过大 */
    private static final int MAX_SCAN_KEYS = 10000;

    /** 列表页值预览最大长度 */
    private static final int PREVIEW_MAX_LEN = 100;

    /**
     * 缓存 key 分页查询（使用 SCAN 非阻塞扫描）
     */
    public PageResult<CacheKeyVO> pageKeys(CacheKeyQuery query) {
        List<String> keys = scanKeys(query.getPattern());
        long total = keys.size();
        int pageNum = Math.max(query.getPageNum(), 1);
        int pageSize = Math.max(query.getPageSize(), 1);
        int from = Math.min((int) (pageNum - 1) * pageSize, keys.size());
        int to = Math.min(from + pageSize, keys.size());
        List<CacheKeyVO> list = new ArrayList<>();
        for (String key : keys.subList(from, to)) {
            list.add(buildKeyVO(key));
        }
        return PageResult.of(list, pageNum, pageSize, total);
    }

    /**
     * 缓存 key 详情
     */
    public CacheValueVO getValue(String key) {
        if (StringUtils.isBlank(key)) {
            throw new BadParameterException("缓存 key 不能为空");
        }
        if (Boolean.FALSE.equals(redisService.hasKey(key))) {
            throw new BadParameterException("缓存 key 不存在：" + key);
        }
        DataType type = redisService.type(key);
        CacheValueVO vo = new CacheValueVO();
        vo.setKey(key);
        vo.setType(type.code());
        vo.setTtl(getTtl(key));
        vo.setValue(toStringValue(readValue(type, key)));
        return vo;
    }

    /**
     * 新增/修改缓存 key 内容及过期时间
     */
    public void setValue(CacheValueDTO dto) {
        String key = dto.getKey();
        if (StringUtils.isBlank(key)) {
            throw new BadParameterException("缓存 key 不能为空");
        }
        Object value = parseValue(dto.getValue() == null ? "" : dto.getValue());
        Long ttl = dto.getTtl();
        if (ttl != null && ttl > 0) {
            redisService.setEx(key, value, ttl, TimeUnit.SECONDS);
        } else {
            redisService.set(key, value);
        }
    }

    /**
     * 批量删除缓存 key
     */
    public void deleteKeys(List<String> keys) {
        if (ObjectUtils.isEmpty(keys)) {
            return;
        }
        redisService.delete(keys);
    }

    /**
     * 设置缓存过期时间
     */
    public void expire(CacheExpireDTO dto) {
        String key = dto.getKey();
        if (StringUtils.isBlank(key)) {
            throw new BadParameterException("缓存 key 不能为空");
        }
        if (dto.getTtl() == null || dto.getTtl() <= 0) {
            throw new BadParameterException("过期时间必须大于0");
        }
        if (Boolean.FALSE.equals(redisService.hasKey(key))) {
            throw new BadParameterException("缓存 key 不存在：" + key);
        }
        redisService.expire(key, dto.getTtl(), TimeUnit.SECONDS);
    }

    /**
     * 取消缓存过期时间（持久化）
     */
    public void persist(String key) {
        if (StringUtils.isBlank(key)) {
            throw new BadParameterException("缓存 key 不能为空");
        }
        if (Boolean.FALSE.equals(redisService.hasKey(key))) {
            throw new BadParameterException("缓存 key 不存在：" + key);
        }
        redisService.persist(key);
    }

    /**
     * 使用 SCAN 非阻塞扫描匹配的 key
     */
    private List<String> scanKeys(String pattern) {
        String match = StringUtils.isBlank(pattern) ? "*" : pattern.trim();
        return redisTemplate.execute((RedisCallback<List<String>>) connection -> {
            List<String> keys = new ArrayList<>();
            try (Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().match(match).count(1000).build())) {
                while (cursor.hasNext()) {
                    byte[] keyBytes = cursor.next();
                    if (keyBytes == null || keyBytes.length == 0) {
                        continue;
                    }
                    keys.add(new String(keyBytes, StandardCharsets.UTF_8));
                    if (keys.size() >= MAX_SCAN_KEYS) {
                        break;
                    }
                }
            } catch (Exception e) {
                throw new BadParameterException("扫描缓存 key 失败: " + e.getMessage());
            }
            return keys;
        });
    }

    /**
     * 构建列表行对象
     */
    private CacheKeyVO buildKeyVO(String key) {
        CacheKeyVO vo = new CacheKeyVO();
        vo.setKey(key);
        try {
            DataType type = redisService.type(key);
            vo.setType(type.code());
            vo.setTtl(getTtl(key));
            vo.setSize(getSize(type, key));
            vo.setValuePreview(buildPreview(type, key));
        } catch (Exception e) {
            log.warn("读取缓存 key[{}] 信息失败: {}", key, e.getMessage());
            vo.setValuePreview("读取失败");
        }
        return vo;
    }

    /**
     * 获取剩余过期时间(秒)，-1 表示永不过期
     */
    private Long getTtl(String key) {
        return redisService.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 获取数据大小
     */
    private Long getSize(DataType type, String key) {
        switch (type) {
            case HASH:
                return redisService.hSize(key);
            case LIST:
                return redisService.lLen(key);
            case SET:
                return redisService.sSize(key);
            case ZSET:
                return redisService.zZCard(key);
            default:
                return redisService.size(key);
        }
    }

    /**
     * 构建值预览
     */
    private String buildPreview(DataType type, String key) {
        if (type == DataType.HASH || type == DataType.LIST
                || type == DataType.SET || type == DataType.ZSET) {
            return "[" + type.code() + "] " + (getSize(type, key) == null ? 0 : getSize(type, key)) + " 个元素";
        }
        String value = toStringValue(readValue(type, key));
        if (value == null) {
            return "";
        }
        return value.length() > PREVIEW_MAX_LEN ? value.substring(0, PREVIEW_MAX_LEN) + "..." : value;
    }

    /**
     * 按数据类型读取值
     */
    private Object readValue(DataType type, String key) {
        try {
            switch (type) {
                case HASH:
                    return redisService.hGetAll(key);
                case LIST:
                    return redisService.lRange(key, 0, -1);
                case SET:
                    return new ArrayList<>(redisService.members(key));
                case ZSET:
                    return zsetValue(key);
                default:
                    return redisService.get(key);
            }
        } catch (Exception e) {
            log.warn("读取缓存 key[{}] 值失败: {}", key, e.getMessage());
            return readRawString(key);
        }
    }

    /**
     * 读取有序集合值
     */
    private Object zsetValue(String key) {
        Set<org.springframework.data.redis.core.ZSetOperations.TypedTuple<Object>> tuples =
                redisService.zRangeWithScores(key, 0, -1);
        List<Map<String, Object>> list = new ArrayList<>();
        if (tuples != null) {
            for (org.springframework.data.redis.core.ZSetOperations.TypedTuple<Object> tuple : tuples) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("value", tuple.getValue());
                item.put("score", tuple.getScore());
                list.add(item);
            }
        }
        return list;
    }

    /**
     * 读取原始字节并转为字符串（值解析失败时的兜底方案）
     */
    private String readRawString(String key) {
        try {
            byte[] bytes = redisTemplate.execute((RedisCallback<byte[]>)
                    connection -> connection.get(key.getBytes(StandardCharsets.UTF_8)));
            return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.warn("读取缓存 key[{}] 原始值失败: {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * 值对象转为展示字符串
     */
    private String toStringValue(Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return (String) value;
        }
        return JSON.toJSONString(value);
    }

    /**
     * 解析值为对象：合法 JSON 解析为对象存储，否则按字符串存储
     */
    private Object parseValue(String value) {
        if (StringUtils.isBlank(value)) {
            return value;
        }
        String trim = value.trim();
        if (trim.startsWith("{") || trim.startsWith("[")) {
            try {
                return JSON.parseObject(trim);
            } catch (JSONException e) {
                // 非合法 JSON，按字符串存储
            }
        }
        return value;
    }
}
