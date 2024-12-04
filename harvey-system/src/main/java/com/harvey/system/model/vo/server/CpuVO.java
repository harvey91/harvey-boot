package com.harvey.system.model.vo.server;

import lombok.Builder;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-12-04 17:25
 **/
@Data
@Builder
public class CpuVO {

    private String name;

    private String packageCount;

    private String core;

    private String coreNumber;

    private String logic;

    private String used;

    private String idle;
}
