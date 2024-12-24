package com.harvey.system.model.vo.server;

import lombok.Builder;
import lombok.Data;

/**
 * @author Harvey
 * @date 2024-12-04 17:25
 **/
@Data
@Builder
public class StorageVO {

    private String total;

    private String available;

    private String used;

    private String usageRate;
}
