-- ----------------------------
-- 信发模块初始化脚本：设备表 / 指令记录表 / 菜单
-- 执行环境：harvey 主库(master)
-- ----------------------------

-- ----------------------------
-- 信发设备表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `screen_device`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `device_no`       varchar(64)  NOT NULL COMMENT '设备编号',
    `device_name`     varchar(64)  NOT NULL COMMENT '设备名称',
    `model`           varchar(64)  DEFAULT '' COMMENT '设备型号',
    `secret`          varchar(255) DEFAULT '' COMMENT '登录令牌',
    `ip`              varchar(64)  DEFAULT '' COMMENT '最后连接IP',
    `last_online_time` datetime(0) DEFAULT NULL COMMENT '最后在线时间',
    `status`          int          DEFAULT '0' COMMENT '状态(0离线 1在线)',
    `remark`          varchar(255) DEFAULT '' COMMENT '描述',
    `sort`            int          DEFAULT '99' COMMENT '排序',
    `enabled`         int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`     datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`         int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_device_no` (`device_no`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='信发设备管理';

-- ----------------------------
-- 信发指令记录表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `screen_command`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `device_id`   bigint(20)  NOT NULL COMMENT '设备id',
    `device_no`   varchar(64) NOT NULL COMMENT '设备编号',
    `cmd_code`    int         NOT NULL COMMENT '指令编码',
    `cmd_name`    varchar(32) DEFAULT '' COMMENT '指令名称',
    `params`      text        DEFAULT NULL COMMENT '指令参数(JSON)',
    `status`      int         DEFAULT '0' COMMENT '状态(0待发送 1已发送 2已执行 3失败 4超时)',
    `seq`         bigint(20)  DEFAULT '0' COMMENT '报文序号(应答关联)',
    `send_time`   datetime(0) DEFAULT NULL COMMENT '发送时间',
    `ack_time`    datetime(0) DEFAULT NULL COMMENT '应答时间',
    `ack_message` varchar(255) DEFAULT '' COMMENT '应答说明',
    `remark`      varchar(255) DEFAULT '' COMMENT '描述',
    `sort`        int         DEFAULT '99' COMMENT '排序',
    `enabled`     int         DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    `update_time` datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`     int         DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_device_no_seq` (`device_no`, `seq`),
    KEY `idx_status` (`status`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='信发指令记录';

-- ----------------------------
-- 信发模块菜单(信发管理 -> 设备管理 / 指令记录)
-- 说明：id 段 1000-1999 预留给信发模块，避免与既有菜单冲突
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1000, 0, '信发管理', 'Screen', 'DIRECTORY', '', '/screen', 'Layout', '', 'Monitor', '/screen/device', 0, 1, '信发模块', 6, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1001, 1000, '设备管理', 'Device', 'MENU', 'ScreenDevice', 'device', 'screen/device/index', 'screen:device:list', 'Connection', '', 0, 1, '信发设备管理', 1, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1002, 1001, '新增设备', '', 'BUTTON', '', '', '', 'screen:device:create', '', '', 0, 1, '', 1, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1003, 1001, '修改设备', '', 'BUTTON', '', '', '', 'screen:device:modify', '', '', 0, 1, '', 2, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1004, 1001, '删除设备', '', 'BUTTON', '', '', '', 'screen:device:delete', '', '', 0, 1, '', 3, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1011, 1000, '指令记录', 'Command', 'MENU', 'ScreenCommand', 'command', 'screen/command/index', 'screen:command:list', 'Document', '', 0, 1, '信发指令记录', 2, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1012, 1001, '下发指令', '', 'BUTTON', '', '', '', 'screen:command:send', '', '', 0, 1, '向设备下发指令', 4, 1, NOW(), NOW(), 1);

-- ----------------------------
-- 信发媒体库表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `screen_media`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `media_name`  varchar(255) NOT NULL COMMENT '媒体名称',
    `media_type`  int          DEFAULT '1' COMMENT '媒体类型(1图片 2视频)',
    `url`         varchar(512) DEFAULT '' COMMENT '访问地址',
    `md5`         varchar(32)  DEFAULT '' COMMENT '文件md5',
    `size`        bigint(20)   DEFAULT '0' COMMENT '文件大小(字节)',
    `suffix`      varchar(32)  DEFAULT '' COMMENT '文件后缀',
    `remark`      varchar(255) DEFAULT '' COMMENT '描述',
    `sort`        int          DEFAULT '99' COMMENT '排序',
    `enabled`     int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time` datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time` datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`     int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_media_type` (`media_type`),
    KEY `idx_md5` (`md5`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='信发媒体库';

-- ----------------------------
-- 信发滚动字幕模板表
-- ----------------------------
CREATE TABLE IF NOT EXISTS `screen_marquee`
(
    `id`           bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `title`        varchar(64)  NOT NULL COMMENT '模板标题',
    `content`      varchar(255) NOT NULL COMMENT '字幕内容',
    `speed`        int          DEFAULT '5' COMMENT '滚动速度(1-20)',
    `color`        varchar(16)  DEFAULT '#ff0000' COMMENT '文字颜色(十六进制)',
    `font_size`    int          DEFAULT '32' COMMENT '字号',
    `repeat_count` int          DEFAULT '0' COMMENT '循环次数(0循环)',
    `remark`       varchar(255) DEFAULT '' COMMENT '描述',
    `sort`         int          DEFAULT '99' COMMENT '排序',
    `enabled`      int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`  datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`      int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='信发滚动字幕模板';

-- ----------------------------
-- 信发模块菜单(补充：媒体库 / 滚动字幕模板)
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1021, 1000, '媒体库', 'Media', 'MENU', 'ScreenMedia', 'media', 'screen/media/index', 'screen:media:list', 'Picture', '', 0, 1, '信发媒体库', 3, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1022, 1021, '上传媒体', '', 'BUTTON', '', '', '', 'screen:media:upload', '', '', 0, 1, '', 1, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1023, 1021, '删除媒体', '', 'BUTTON', '', '', '', 'screen:media:delete', '', '', 0, 1, '', 2, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1024, 1021, '推送媒体', '', 'BUTTON', '', '', '', 'screen:media:push', '', '', 0, 1, '', 3, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1031, 1000, '滚动字幕', 'Marquee', 'MENU', 'ScreenMarquee', 'marquee', 'screen/marquee/index', 'screen:marquee:list', 'MessageBox', '', 0, 1, '信发滚动字幕模板', 4, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1032, 1031, '新增模板', '', 'BUTTON', '', '', '', 'screen:marquee:create', '', '', 0, 1, '', 1, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1033, 1031, '修改模板', '', 'BUTTON', '', '', '', 'screen:marquee:modify', '', '', 0, 1, '', 2, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1034, 1031, '删除模板', '', 'BUTTON', '', '', '', 'screen:marquee:delete', '', '', 0, 1, '', 3, 1, NOW(), NOW(), 1);

INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1035, 1031, '下发字幕', '', 'BUTTON', '', '', '', 'screen:marquee:send', '', '', 0, 1, '', 4, 1, NOW(), NOW(), 1);

-- 若需要给非超级管理员角色授权，示例(替换 role_id 为目标角色id)：
-- INSERT INTO `sys_role_menu` (`role_id`, `menu_id`, `create_time`) VALUES (2, 1000, NOW()), (2, 1001, NOW()), (2, 1002, NOW()), (2, 1003, NOW()), (2, 1004, NOW()), (2, 1011, NOW()), (2, 1012, NOW()), (2, 1021, NOW()), (2, 1022, NOW()), (2, 1023, NOW()), (2, 1024, NOW()), (2, 1031, NOW()), (2, 1032, NOW()), (2, 1033, NOW()), (2, 1034, NOW()), (2, 1035, NOW());
