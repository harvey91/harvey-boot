package com.harvey.system.domain.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Harvey
 * @date 2024-10-31 20:05
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class DictQueryParam extends BaseQueryParam {

    private String dictCode;
}
