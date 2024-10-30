
# DROP TABLE IF EXISTS sys_user_dept;

-- 系统用户表
CREATE TABLE `sys_user`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键，用户id',
    `username`        varchar(16)  NOT NULL COMMENT '用户名',
    `password`        varchar(255) NOT NULL COMMENT '密码',
    `nickname`        varchar(16)  DEFAULT '' COMMENT '昵称',
    `avatar`          varchar(255) DEFAULT '' COMMENT '头像',
    `gender`          int(1)       DEFAULT '0' COMMENT '性别：0未知，1男，2女',
    `email`           varchar(64)  DEFAULT '' COMMENT '邮箱',
    `phone`           varchar(16)  DEFAULT '' COMMENT '手机号',
    `dept_id`         bigint(20)   DEFAULT '0' COMMENT '所属部门id',
    `last_login_ip`   varchar(255) DEFAULT '' COMMENT '最后登录IP',
    `last_login_time` datetime(0)  DEFAULT NULL COMMENT '最后登录时间',
    `enabled`         int(1)       DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`     datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`         int(1)       DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE (`username`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户表';

-- 系统部门表
CREATE TABLE `sys_dept`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键，部门id',
    `parent_id`   bigint(20)  NOT NULL COMMENT '父级id',
    `dept_name`   varchar(64) NOT NULL COMMENT '部门名称',
    `remark`      varchar(255) DEFAULT '' COMMENT '部门描述',
    `sort_num`     int(11)      DEFAULT '99' COMMENT '',
    `enabled`     int(1)       DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int(1)       DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='系统部门表';

-- 系统角色表
CREATE TABLE `sys_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键，角色id',
    `role_code`   varchar(32) NOT NULL COMMENT '角色编码',
    `role_name`   varchar(32) NOT NULL COMMENT '角色名称',
    `remark` varchar(16) DEFAULT '' COMMENT '角色描述',
    `sort_num`     int(11)      DEFAULT '99' COMMENT '',
    `enabled`     int(1)      DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int(1)      DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色表';

-- 系统菜单表
CREATE TABLE `sys_menu`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键，菜单id',
    `parent_id`          bigint(20)  NOT NULL COMMENT '上级id',
    `menu_name`    varchar(64) NOT NULL COMMENT '菜单名称',
    `menu_name_en` varchar(64) NOT NULL COMMENT '菜单英文名称',
    `type`         varchar(16) DEFAULT '' COMMENT '菜单类型',
    `route_name`   varchar(64) DEFAULT '' COMMENT '路由名称',
    `route_path`   varchar(64) DEFAULT '' COMMENT '路由路径',
    `component`    varchar(64) DEFAULT '' COMMENT '组件路径',
    `permission`   varchar(64) DEFAULT '' COMMENT '权限标识',
    `icon`         varchar(32) DEFAULT '' COMMENT '图标',
    `redirect`     varchar(255) DEFAULT '' COMMENT '重定向地址',
    `sort_num`     int(11)      DEFAULT '99' COMMENT '排序',
    `enabled`      int(1)       DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`  datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`      int(1)       DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='系统菜单表';

-- 系统用户角色关联表
CREATE TABLE `sys_dept_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `dept_id`     bigint(20)  NOT NULL COMMENT '部门id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='系统部门角色关联表';

-- 系统角色菜单关联表
CREATE TABLE `sys_role_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `menu_id`     bigint(20)  NOT NULL COMMENT '菜单id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色菜单关联表';
