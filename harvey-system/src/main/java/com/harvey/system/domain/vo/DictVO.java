package com.harvey.system.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 20:53
 **/
@Data
public class DictVO {

    private String name;

    private String dictCode;

    private List<DataVO> dictDataList;

    @Data
    public static class DataVO {

        private String value;

        private String label;

        private String tag;
    }
}
