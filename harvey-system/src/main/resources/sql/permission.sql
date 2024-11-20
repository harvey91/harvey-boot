# DROP TABLE IF EXISTS sys_user_dept;

CREATE TABLE `sys_user`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `username`        varchar(16)  NOT NULL COMMENT '用户名',
    `password`        varchar(255) NOT NULL COMMENT '密码',
    `nickname`        varchar(16)  DEFAULT '' COMMENT '昵称',
    `avatar`          varchar(255) DEFAULT '' COMMENT '头像',
    `gender`          int          DEFAULT '0' COMMENT '性别：0未知，1男，2女',
    `email`           varchar(64)  DEFAULT '' COMMENT '邮箱',
    `phone`           varchar(16)  DEFAULT '' COMMENT '手机号',
    `dept_id`         bigint(20)   DEFAULT '0' COMMENT '所属部门id',
    `last_login_ip`   varchar(255) DEFAULT '' COMMENT '最后登录IP',
    `last_login_time` datetime(0)  DEFAULT NULL COMMENT '最后登录时间',
    `remark`          varchar(255) DEFAULT '' COMMENT '描述',
    `enabled`         int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `sort`            int          DEFAULT '0' COMMENT '排序',
    `create_time`     datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`         int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE (`username`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户表';

CREATE TABLE `sys_dept`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `parent_id`   bigint(20)  NOT NULL COMMENT '父级id',
    `dept_name`   varchar(64) NOT NULL COMMENT '部门名称',
    `dept_code`   varchar(64)  DEFAULT '' COMMENT '部门编号',
    `remark`      varchar(255) DEFAULT '' COMMENT '描述',
    `sort`        int          DEFAULT '99' COMMENT '排序',
    `enabled`     int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统部门表';

CREATE TABLE `sys_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_code`   varchar(32) NOT NULL COMMENT '角色编码',
    `role_name`   varchar(32) NOT NULL COMMENT '角色名称',
    `data_scope`  int          DEFAULT '3' COMMENT '数据权限：0全部数据，1部门及子部门数据，2本部门数据，3本人数据',
    `remark`      varchar(255) DEFAULT '' COMMENT '描述',
    `sort`        int          DEFAULT '99' COMMENT '排序',
    `enabled`     int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色表';

CREATE TABLE `sys_menu`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `parent_id`    bigint(20)  NOT NULL COMMENT '上级id',
    `menu_name`    varchar(64) NOT NULL COMMENT '菜单名称',
    `menu_name_en` varchar(64)  DEFAULT '' COMMENT '菜单英文名称',
    `type`         varchar(16)  DEFAULT '' COMMENT '菜单类型：DIRECTORY目录，MENU菜单，BUTTON按钮，LINK链接',
    `route_name`   varchar(64)  DEFAULT '' COMMENT '路由名称',
    `route_path`   varchar(64)  DEFAULT '' COMMENT '路由路径',
    `component`    varchar(64)  DEFAULT '' COMMENT '组件路径',
    `permission`   varchar(64)  DEFAULT '' COMMENT '权限标识',
    `icon`         varchar(32)  DEFAULT '' COMMENT '图标',
    `redirect`     varchar(255) DEFAULT '' COMMENT '重定向地址',
    `always_show`  int          DEFAULT '0' COMMENT '始终显示',
    `keep_alive`   int          DEFAULT '1' COMMENT '缓存页面',
    `remark`       varchar(255) DEFAULT '' COMMENT '描述',
    `sort`         int          DEFAULT '99' COMMENT '排序',
    `enabled`      int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`  datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`      int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统菜单表';

CREATE TABLE `sys_user_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统用户角色关联表';

CREATE TABLE `sys_role_dept`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `dept_id`     bigint(20)  NOT NULL COMMENT '部门id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色部门关联表';

CREATE TABLE `sys_role_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `menu_id`     bigint(20)  NOT NULL COMMENT '菜单id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统角色菜单关联表';

CREATE TABLE `sys_dict`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `dict_code`   varchar(32) NOT NULL COMMENT '字典编码',
    `dict_name`   varchar(32) NOT NULL COMMENT '字典名称',
    `remark`      varchar(255) DEFAULT '' COMMENT '描述',
    `sort`        int          DEFAULT '99' COMMENT '排序',
    `enabled`     int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统字典表';

CREATE TABLE `sys_dict_data`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `dict_code`   varchar(32)  NOT NULL COMMENT '字典编码',
    `label`       varchar(255) NOT NULL COMMENT '字典项',
    `value`       varchar(32)  NOT NULL COMMENT '字典值',
    `tag`         varchar(32)  DEFAULT '' COMMENT '前端标签tag值',
    `remark`      varchar(255) DEFAULT '' COMMENT '备注',
    `sort`        int          DEFAULT '99' COMMENT '排序',
    `enabled`     int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time` datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`     int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统字典数据表';

CREATE TABLE `sys_post`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `post_name`   varchar(64) NOT NULL COMMENT '职位名称',
    `post_level`  int         NOT NULL COMMENT '职级',
    `remark`      varchar(255) DEFAULT '' COMMENT '备注',
    `sort`        int          DEFAULT '99' COMMENT '排序',
    `enabled`     int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统职位表';

CREATE TABLE `sys_config`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `config_name`  varchar(64) NOT NULL COMMENT '配置名称',
    `config_key`   varchar(64) NOT NULL COMMENT '配置键key',
    `config_value` varchar(64) NOT NULL COMMENT '配置值value',
    `remark`       varchar(255) DEFAULT '' COMMENT '备注',
    `sort`         int          DEFAULT '99' COMMENT '排序',
    `enabled`      int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`  datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`      int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE (`config_key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统配置表';

CREATE TABLE `sys_online_user`
(
    `uuid`        varchar(32) NOT NULL COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `username`    varchar(32) DEFAULT '' COMMENT '用户名',
    `dept_name`   varchar(32) DEFAULT '' COMMENT '所属部门',
    `ip`          varchar(32) DEFAULT '' COMMENT '登录IP',
    `location`    varchar(32) DEFAULT '' COMMENT '登录地点',
    `browser`     varchar(32) DEFAULT '' COMMENT '浏览器',
    `os`          varchar(32) DEFAULT '' COMMENT '操作系统',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `expire_time` datetime(0) NOT NULL COMMENT '过期时间',
    `status`      int         DEFAULT '0' COMMENT '在线状态',
    PRIMARY KEY (`uuid`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统在线用户表';

CREATE TABLE `sys_notice`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `title`        varchar(255) NOT NULL COMMENT '通知标题',
    `content`      text COMMENT '通知内容',
    `type`         int          NOT NULL COMMENT '通知类型',
    `status`       int          NOT NULL COMMENT '通知状态(0未发布，1已发布，2已撤回)',
    `level`        varchar(16)  NOT NULL COMMENT '通知等级',
    `target_type`  int          DEFAULT '1' COMMENT '通知目标类型(1全体，2指定人)',
    `publisher_id` bigint(20)   DEFAULT NULL COMMENT '发布人id',
    `publish_time` datetime(0)  DEFAULT NULL COMMENT '发布时间',
    `revoke_time`  datetime(0)  DEFAULT NULL COMMENT '撤回时间',
    `remark`       varchar(255) DEFAULT '' COMMENT '备注',
    `sort`         int          DEFAULT '99' COMMENT '排序',
    `enabled`      int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`  datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`      int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统通知表';

CREATE TABLE `sys_notice_user`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `notice_id`   bigint(20)  NOT NULL COMMENT '通知id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统通知指定用户表';


CREATE TABLE `sys_notice_read`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `notice_id`   bigint(20)  NOT NULL COMMENT '通知id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统通知已读用户表';