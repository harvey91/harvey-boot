package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 通知发布状态 枚举类
 * @author Harvey
 * @date 2024-11-20 18:07
 **/
@Getter
@AllArgsConstructor
public enum PublishStatusEnum {

    UNPUBLISHED(0, "未发布"),
    PUBLISHED(1, "已发布"),
    REVOKED(2, "已撤回"),
    ;

    private final int value;

    private final String label;

    public static Map<Integer, String> map = new HashMap<>();
    static {
        Arrays.stream(PublishStatusEnum.values()).forEach(item -> map.put(item.value, item.label));
    }
}
