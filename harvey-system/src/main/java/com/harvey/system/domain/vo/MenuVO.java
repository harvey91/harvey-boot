package com.harvey.system.domain.vo;

import com.harvey.system.entity.SysMenu;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 10:56
 **/
@Data
public class MenuVO extends SysMenu {

    private List<MenuVO> children;
}
