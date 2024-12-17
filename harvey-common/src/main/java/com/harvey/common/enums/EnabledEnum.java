package com.harvey.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Harvey
 * @date 2024-12-12 21:45
 **/
@Getter
@AllArgsConstructor
public enum EnabledEnum {

    ENABLE(1), DISABLE(0);

    private final int value;
}
