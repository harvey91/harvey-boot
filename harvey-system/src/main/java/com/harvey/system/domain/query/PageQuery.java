package com.harvey.system.domain.query;

import lombok.Data;

/**
 * @author Harvey
 * @date 2024-10-31 20:06
 **/
@Data
public class PageQuery {

    private int pageNum = 1;

    private int pageSize = 10;
}
