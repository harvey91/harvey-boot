package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通知目标类型 枚举类
 * @author Harvey
 * @date 2024-11-20 18:07
 **/
@Getter
@AllArgsConstructor
public enum NoticeTargetTypeEnum {

    ALL(1, "全体"),
    TARGET(2, "指定"),
    ;

    private final int value;

    private final String label;

    public static NoticeTargetTypeEnum get(int value) {
        for (NoticeTargetTypeEnum enum1 : NoticeTargetTypeEnum.values()) {
            if (enum1.getValue() == value) {
                return enum1;
            }
        }
        return null;
    }
}
