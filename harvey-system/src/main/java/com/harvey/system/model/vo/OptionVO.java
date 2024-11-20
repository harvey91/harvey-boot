package com.harvey.system.model.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 下拉列表-可选树形
 * @author Harvey
 * @date 2024-10-30 13:54
 **/
@Data
@Builder
public class OptionVO {
    private Long value;

    private String label;

    private List<OptionVO> children;

}
