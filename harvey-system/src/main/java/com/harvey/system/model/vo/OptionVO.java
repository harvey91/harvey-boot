package com.harvey.system.model.vo;

import lombok.Data;

import java.util.List;

/**
 * 下拉树列表
 * @author Harvey
 * @date 2024-10-30 13:54
 **/
@Data
public class OptionVO {
    private Long value;

    private String label;

    private List<OptionVO> children;
}
