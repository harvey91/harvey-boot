package com.harvey.system.domain.vo;

import com.harvey.system.entity.SysDept;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-29 18:36
 **/
@Data
public class DeptVO extends SysDept {

    private List<DeptVO> children;
}
