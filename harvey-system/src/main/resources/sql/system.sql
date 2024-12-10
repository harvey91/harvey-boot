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
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户管理';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门管理';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色管理';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单管理';

CREATE TABLE `sys_user_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联';

CREATE TABLE `sys_role_dept`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `dept_id`     bigint(20)  NOT NULL COMMENT '部门id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色部门关联';

CREATE TABLE `sys_role_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `menu_id`     bigint(20)  NOT NULL COMMENT '菜单id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色菜单关联';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典管理';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典数据';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='职位管理';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='系统配置';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='在线用户';

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
  DEFAULT CHARSET = utf8mb4 COMMENT ='通知公告';

CREATE TABLE `sys_notice_user`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `notice_id`   bigint(20)  NOT NULL COMMENT '通知id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `is_read`     tinyint(2) DEFAULT '0' COMMENT '是否已读',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `read_time`   datetime(0) NOT NULL COMMENT '已读时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='通知指定用户';

CREATE TABLE `sys_log_op`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '操作用户id',
    `operator`    varchar(32)  DEFAULT '' COMMENT '操作员',
    `module`      varchar(32)  DEFAULT '' COMMENT '模块',
    `request_uri` varchar(255) DEFAULT '' COMMENT '请求路径',
    `method`      varchar(128) DEFAULT '' COMMENT '请求方法',
    `param`       varchar(255) DEFAULT '' COMMENT '请求参数',
    `detail`      text COMMENT '详情',
    `duration`    bigint       DEFAULT '0' COMMENT '执行时长(ms)',
    `ip`          varchar(32)  DEFAULT '' COMMENT 'IP',
    `location`    varchar(32)  DEFAULT '' COMMENT '地点',
    `browser`     varchar(32)  DEFAULT '' COMMENT '浏览器',
    `os`          varchar(32)  DEFAULT '' COMMENT '操作系统',
    `remark`      varchar(255) DEFAULT '' COMMENT '备注',
    `result`      tinyint(2)   DEFAULT '1' COMMENT '结果(1成功，2异常)',
    `sort`        int          DEFAULT '0' COMMENT '排序',
    `enabled`     tinyint(2)   DEFAULT '1' COMMENT '是否启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     tinyint(2)   DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='操作日志';

CREATE TABLE `sys_log_login`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `username`    varchar(32)  DEFAULT '' COMMENT '用户名',
    `ip`          varchar(32)  DEFAULT '' COMMENT 'IP',
    `location`    varchar(32)  DEFAULT '' COMMENT '地点',
    `browser`     varchar(32)  DEFAULT '' COMMENT '浏览器',
    `os`          varchar(32)  DEFAULT '' COMMENT '操作系统',
    `result`      varchar(16)  DEFAULT '' COMMENT '结果',
    `remark`      varchar(255) DEFAULT '' COMMENT '备注',
    `sort`        int          DEFAULT '0' COMMENT '排序',
    `enabled`     tinyint(2)   DEFAULT '1' COMMENT '是否启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     tinyint(2)   DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='登陆日志';

CREATE TABLE `sys_file_manage`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)   NOT NULL COMMENT '上传者id',
    `name`        varchar(255) NOT NULL COMMENT '文件名',
    `md5`         varchar(32)  NOT NULL COMMENT 'MD5',
    `size`        bigint(20)   NOT NULL COMMENT '文件大小',
    `suffix`      varchar(32)  DEFAULT '' COMMENT '文件后缀',
    `platform`    varchar(32)  DEFAULT '' COMMENT '上传平台',
    `path`        varchar(255) DEFAULT '' COMMENT '文件路径',
    `remark`      varchar(255) DEFAULT '' COMMENT '备注',
    `sort`        int          DEFAULT '0' COMMENT '排序',
    `enabled`     tinyint(2)   DEFAULT '1' COMMENT '是否启用',
    `create_time` datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time` datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`     tinyint(2)   DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='文件管理';