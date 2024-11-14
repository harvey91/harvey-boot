package com.harvey.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 系统菜单表
 * </p>
 *
 * @author harvey
 * @since 2024-10-30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_menu")
@ApiModel(value = "SysMenu对象", description = "系统菜单表")
public class SysMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("主键，菜单id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("上级id")
    private Long parentId;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单英文名称")
    private String menuNameEn;

    @ApiModelProperty("菜单类型")
    private String type;

    @ApiModelProperty("路由名称")
    private String routeName;

    @ApiModelProperty("路由路径")
    private String routePath;

    @ApiModelProperty("组件路径")
    private String component;

    @ApiModelProperty("权限标识")
    private String permission;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("重定向地址")
    private String redirect;

    @ApiModelProperty("始终显示")
    private Integer alwaysShow;

    @ApiModelProperty("缓存页面")
    private Integer keepAlive;

    @ApiModelProperty("排序")
    private Integer sort;

}
