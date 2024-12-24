package com.harvey.common.model.query;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 20:06
 **/
@Data
public class Query {

    private int pageNum = 1;

    private int pageSize = 10;

    private String keywords;

    private Integer enabled;

    private List<LocalDateTime> createTime;
}
