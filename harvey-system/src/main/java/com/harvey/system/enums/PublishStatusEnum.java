package com.harvey.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

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

    public static PublishStatusEnum get(int value) {
        for (PublishStatusEnum enum1 : PublishStatusEnum.values()) {
            if (enum1.getValue() == value) {
                return enum1;
            }
        }
        return null;
    }
}
