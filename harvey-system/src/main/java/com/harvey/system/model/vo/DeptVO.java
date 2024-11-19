package com.harvey.system.model.vo;

import com.harvey.system.model.entity.Dept;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-29 18:36
 **/
@Data
public class DeptVO extends Dept {

    private List<DeptVO> children;
}
