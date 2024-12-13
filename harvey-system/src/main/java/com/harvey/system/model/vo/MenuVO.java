package com.harvey.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Harvey
 * @date 2024-10-31 10:56
 **/
@Data
public class MenuVO {

    private Long id;

    @Schema(title = "parentId", description = "上级id")
    private Long parentId;

    @Schema(title = "menuName", description = "菜单名称")
    private String menuName;

    @Schema(title = "menuNameEn", description = "菜单英文名称")
    private String menuNameEn;

    @Schema(title = "type", description = "菜单类型")
    private String type;

    @Schema(title = "routeName", description = "路由名称")
    private String routeName;

    @Schema(title = "routePath", description = "路由路径")
    private String routePath;

    @Schema(title = "component", description = "组件路径")
    private String component;

    @Schema(title = "permission", description = "权限标识")
    private String permission;

    @Schema(title = "icon", description = "图标")
    private String icon;

    @Schema(title = "redirect", description = "重定向地址")
    private String redirect;

    @Schema(title = "alwaysShow", description = "始终显示")
    private Integer alwaysShow;

    @Schema(title = "keepAlive", description = "缓存页面")
    private Integer keepAlive;

    @Schema(title = "remark", description = "描述")
    private String remark;

    @Schema(title = "sort", description = "排序", defaultValue = "99")
    private Integer sort;

    @Schema(title = "enabled", description = "是否启用(0禁用，1启用)", defaultValue = "1")
    private Integer enabled;

    @Schema(title = "createTime", description = "创建时间")
    public LocalDateTime createTime;

    private List<MenuVO> children;
}
