package com.harvey.core.model;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-29 10:06
 **/
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PageResult<T> {

    private long current;

    private long size;

    private long total;

    private List<T> list;

    public PageResult(List<T> list, long current, long size, long total) {
        this.list = list;
        this.current = current;
        this.size = size;
        this.total = total;
    }

    public static <T> PageResult<T> of(List<T> list, long current, long size, long total) {
        return new PageResult<T>(list, current, size, total);
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return of(page.getRecords(), page.getCurrent(), page.getSize(), page.getTotal());
    }

    public static <T> PageResult<T> of(List<T> list, Page<T> page) {
        return of(list, page.getCurrent(), page.getSize(), page.getTotal());
    }

}
