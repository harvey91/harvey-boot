package com.harvey.system.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 动态路由
 * @author Harvey
 * @date 2024-10-31 10:28
 **/
@Data
public class RouteVO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String path;

    /** 组件路由 */
    private String component;

    /** 重定向地址 */
    private String redirect;

    /** 路由名称 */
    private String name;

    private Meta meta;

    private List<RouteVO> children;

    @Data
    public static class Meta implements Serializable {
        @Serial
        private static final long serialVersionUID = 1L;

        /** 菜单名称 */
        private String title;

        /** 图标 */
        private String icon;

        /** 侧边栏显示 */
        private Boolean hidden;

        private Boolean keepAlive;

        /** 一直显示根路由 */
        private Boolean alwaysShow;

        private String params;
    }
}


