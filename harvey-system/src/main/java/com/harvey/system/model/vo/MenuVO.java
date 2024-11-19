package com.harvey.system.model.vo;

import com.harvey.system.model.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 10:56
 **/
@Data
public class MenuVO extends Menu {

    private List<MenuVO> children;
}
