-- 系统用户表
CREATE TABLE `sys_user`
(
    `id`              bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，用户id',
    `username`        varchar(16)  NOT NULL COMMENT '用户名',
    `password`        varchar(255) NOT NULL COMMENT '密码',
    `nickname`        varchar(16)  DEFAULT '' COMMENT '昵称',
    `avatar`          varchar(255) DEFAULT '' COMMENT '头像',
    `sex`             int(1) DEFAULT '0' COMMENT '性别：0未知，1男，2女',
    `last_login_ip`   varchar(255) DEFAULT '' COMMENT '最后登录IP',
    `last_login_time` datetime(0) DEFAULT NULL COMMENT '最后登录时间',
    `enabled`         int(1) DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`     datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`         int(1) DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- 系统部门表
CREATE TABLE `sys_dept`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，部门id',
    `parent_id`         bigint(20) NOT NULL COMMENT '父级id',
    `dept_name`        varchar(64) NOT NULL COMMENT '部门名称',
    `remark`      varchar(255) DEFAULT '' COMMENT '部门描述',
    `enabled`     int(1) DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int(1) DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统部门表';

-- 系统角色表
CREATE TABLE `sys_role`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，角色id',
    `role_code`   varchar(32) NOT NULL COMMENT '角色编码',
    `role_name`   varchar(32) NOT NULL COMMENT '角色名称',
    `role_remark` varchar(16) DEFAULT '' COMMENT '角色描述',
    `enabled`     int(1) DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int(1) DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统角色表';

-- 系统菜单表
CREATE TABLE `sys_menu`
(
    `id`           bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键，菜单id',
    `pid`          bigint(20) NOT NULL COMMENT '父id',
    `menu_name`    varchar(64) NOT NULL COMMENT '菜单名称',
    `menu_name_en` varchar(64) NOT NULL COMMENT '菜单英文名称',
    `icon`         varchar(255) DEFAULT '' COMMENT '图标',
    `route`        varchar(255) DEFAULT '' COMMENT '路由',
    `level`        int(11) DEFAULT '1' COMMENT '菜单等级',
    `sort_num`     int(11) DEFAULT '99' COMMENT '',
    `enabled`     int(1) DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`  datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`      int(1) DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统菜单表';

-- 系统用户角色关联表
# DROP TABLE IF EXISTS sys_user_dept;
CREATE TABLE `sys_user_dept`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     bigint(20) NOT NULL COMMENT '用户id',
    `dept_id`     bigint(20) NOT NULL COMMENT '部门id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统用户部门关联表';

-- 系统角色菜单关联表
CREATE TABLE `sys_role_menu`
(
    `id`          bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `role_id`     bigint(20) NOT NULL COMMENT '角色id',
    `menu_id`     bigint(20) NOT NULL COMMENT '菜单id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='系统角色菜单关联表';
