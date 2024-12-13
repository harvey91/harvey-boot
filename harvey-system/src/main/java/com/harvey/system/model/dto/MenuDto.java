package com.harvey.system.model.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.harvey.system.model.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 菜单管理
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Data
@Schema(title = "MenuDto")
public class MenuDto {

    @Schema(title = "parentId", description = "上级id")
    private Long parentId;

    @NotBlank(message = "菜单名称不能为空")
    @Schema(title = "menuName", description = "菜单名称")
    private String menuName;

    @Schema(title = "menuNameEn", description = "菜单英文名称")
    private String menuNameEn;

    @NotBlank(message = "菜单类型不能为空")
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

}
