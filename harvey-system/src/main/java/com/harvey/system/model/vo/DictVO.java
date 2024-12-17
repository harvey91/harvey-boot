package com.harvey.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 20:53
 **/
@Data
public class DictVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String name;

    private String dictCode;

    private List<DataVO> dictDataList;

    @Data
    public static class DataVO implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        private String value;

        private String label;

        private String tag;
    }
}
