package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 角色数据范围 枚举类
 * @author Harvey
 * @date 2024-11-07 18:07
 **/
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    ALL(0, "全部数据"),
    DEPT_CHILDREN(1, "部门及子部门数据"),
    DEPT(2, "本部门数据"),
    PERSON(3, "本人数据"),
    CUSTOM(4, "自定义")
    ;

    private final int value;

    private final String label;

    public static DataScopeEnum get(int value) {
        for (DataScopeEnum dataScopeEnum : DataScopeEnum.values()) {
            if (dataScopeEnum.getValue() == value) {
                return dataScopeEnum;
            }
        }
        return null;
    }
}
