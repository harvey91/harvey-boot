/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 90100
 Source Host           : localhost:3306
 Source Schema         : harvey

 Target Server Type    : MySQL
 Target Server Version : 90100
 File Encoding         : 65001

 Date: 07/11/2024 10:24:07
 注：已合并 system.sql 最新表结构（sys_user/sys_menu/sys_dict_data 补 remark、sort 列，
     sys_log_op.param 改名为 params，sys_dept_role 修正为 sys_role_dept）
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
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
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户管理';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` (`id`, `username`, `password`, `nickname`, `avatar`, `gender`, `email`, `phone`, `dept_id`, `last_login_ip`, `last_login_time`, `remark`, `enabled`, `sort`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'admin', '$2a$10$3eQN7NGT1e0wLr.RQGxsVuak2JyWz/0Ky.WVOVUsAd7/t5muiviEu', '管理员', 'https://gitee.com/harvey-enterprise-level/harvey-boot/raw/master/harvey-system/src/main/resources/static/avatar.png', 1, '11@qq.com', '18508945529', 5, '', '2024-10-30 22:14:33', '', 1, 0, '2024-10-30 22:14:37', '2024-11-04 23:29:11', 1);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
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
  AUTO_INCREMENT = 10
  DEFAULT CHARSET = utf8mb4 COMMENT ='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` (`id`, `parent_id`, `dept_name`, `dept_code`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (5, 0, '总公司', '1', '', 99, 1, '2024-10-30 17:27:34', '2024-10-30 17:27:34', 1);
INSERT INTO `sys_dept` (`id`, `parent_id`, `dept_name`, `dept_code`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (7, 0, '分公司1', '', '', 99, 1, '2024-10-30 17:27:53', '2024-10-30 17:27:53', 1);
INSERT INTO `sys_dept` (`id`, `parent_id`, `dept_name`, `dept_code`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (8, 5, '研发部', '', '', 99, 1, '2024-10-30 17:28:02', '2024-10-30 17:28:02', 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
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
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色管理';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `data_scope`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'admin', '管理员', 0, '', 1, 1, '2024-10-31 08:37:30', '2024-10-31 08:37:30', 1);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `data_scope`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (2, 'dev', '开发人员', 1, '', 2, 1, '2024-10-31 08:39:02', '2024-10-31 08:39:02', 1);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `data_scope`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (3, '21', '产品经理', 2, '', 3, 1, '2024-10-31 13:24:19', '2024-10-31 13:24:19', 1);
INSERT INTO `sys_role` (`id`, `role_code`, `role_name`, `data_scope`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (4, 'em', '普通员工', 3, '', 4, 1, '2024-11-05 09:48:46', '2024-11-05 09:48:46', 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
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
  AUTO_INCREMENT = 216
  DEFAULT CHARSET = utf8mb4 COMMENT ='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1, 0, '系统管理', 'System', 'DIRECTORY', '', '/system', 'Layout', '', 'system', '/system/user', 0, 1, '', 2, 1, '2024-10-31 10:44:59', '2024-10-31 10:45:02', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (2, 1, '用户管理', 'User', 'MENU', 'User', 'user', 'system/user/index', '', 'el-icon-User', '', 0, 1, '', 1, 1, '2024-10-31 10:46:43', '2024-10-31 10:46:56', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (3, 1, '部门管理', 'Dept', 'MENU', 'Dept', 'dept', 'system/dept/index', '', 'role', '', 0, 1, '', 2, 1, '2024-10-31 10:46:47', '2024-10-31 10:46:58', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (4, 1, '角色管理', 'Role', 'MENU', 'Role', 'role', 'system/role/index', '', 'tree', '', 0, 1, '', 3, 1, '2024-10-31 10:46:49', '2024-10-31 10:47:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (5, 1, '菜单管理', 'Menu', 'MENU', 'Menu', 'menu', 'system/menu/index', '', 'menu', '', 0, 1, '', 4, 1, '2024-10-31 10:46:54', '2024-10-31 10:47:01', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (6, 1, '字典管理', '', 'MENU', 'Dict', 'dict', 'system/dict/index', '', 'dict', '', 0, 1, '', 5, 1, '2024-10-31 17:21:12', '2024-10-31 17:21:12', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (7, 1, '字典数据', '', 'MENU', 'DictData', 'dict-data', 'system/dict/data', '', 'dict', '', 0, 1, '', 6, 0, '2024-10-31 19:57:53', '2024-10-31 19:57:53', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (8, 2, '新增用户', '', 'BUTTON', '', '', '', 'sys:user:create', '', '', 0, 1, '', 1, 1, '2024-11-06 21:02:41', '2024-11-06 21:02:41', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (10, 0, '服务监控', '', 'DIRECTORY', '', '/monitor', 'Layout', '', 'client', '/monitor/server', 0, 1, '', 9, 1, '2024-11-06 21:33:55', '2024-11-06 21:33:55', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (11, 1, '职位管理', '', 'MENU', 'Post', 'post', 'system/post/index', '', 'table', '', 0, 1, '', 7, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (12, 1, '配置管理', '', 'MENU', 'Config', 'config', 'system/config/index', '', 'setting', '', 0, 1, '', 8, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (13, 1, '通知公告', '', 'MENU', 'Notice', 'notice', 'system/notice/index', '', 'message', '', 0, 1, '', 9, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (14, 10, '在线用户', '', 'MENU', 'OnlineUser', 'online-user', 'system/online-user/index', '', 'user', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (15, 10, '操作日志', '', 'MENU', 'LogOp', 'log-op', 'system/log/ex', '', 'todo', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (16, 10, '登录日志', '', 'MENU', 'LogLogin', 'log-login', 'system/log/login', '', 'document', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (17, 10, '服务监控', '', 'MENU', 'ServerMonitor', 'server', 'monitor/server/index', '', 'monitor', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (18, 10, 'Druid 监控', '', 'MENU', 'Druid', 'druid', 'monitor/druid/index', '', 'code', '', 0, 1, '', 5, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (19, 0, '工具', '', 'DIRECTORY', '', '/tool', 'Layout', '', 'code', '/tool/file', 0, 1, '', 8, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (20, 19, '文件管理', '', 'MENU', 'File', 'file', 'tool/file/index', '', 'file', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (21, 20, '查询', '', 'BUTTON', '', '', '', 'sys:file:manage:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (22, 20, '上传', '', 'BUTTON', '', '', '', 'sys:file:manage:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (23, 20, '下载', '', 'BUTTON', '', '', '', 'sys:file:manage:download', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (24, 20, '修改', '', 'BUTTON', '', '', '', 'sys:file:manage:modify', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (25, 20, '删除', '', 'BUTTON', '', '', '', 'sys:file:manage:delete', '', '', 0, 1, '', 5, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (26, 2, '查询', '', 'BUTTON', '', '', '', 'sys:user:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (27, 2, '修改', '', 'BUTTON', '', '', '', 'sys:user:modify', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (28, 2, '删除', '', 'BUTTON', '', '', '', 'sys:user:delete', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (29, 2, '重置密码', '', 'BUTTON', '', '', '', 'sys:user:password:reset', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (30, 3, '查询', '', 'BUTTON', '', '', '', 'sys:dept:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (31, 3, '新增', '', 'BUTTON', '', '', '', 'sys:dept:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (32, 3, '修改', '', 'BUTTON', '', '', '', 'sys:dept:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (33, 3, '删除', '', 'BUTTON', '', '', '', 'sys:dept:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (34, 4, '查询', '', 'BUTTON', '', '', '', 'sys:role:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (35, 4, '新增', '', 'BUTTON', '', '', '', 'sys:role:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (36, 4, '修改', '', 'BUTTON', '', '', '', 'sys:role:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (37, 4, '删除', '', 'BUTTON', '', '', '', 'sys:role:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (38, 4, '分配权限', '', 'BUTTON', '', '', '', 'sys:role:perm', '', '', 0, 1, '', 5, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (39, 5, '查询', '', 'BUTTON', '', '', '', 'sys:menu:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (40, 5, '新增', '', 'BUTTON', '', '', '', 'sys:menu:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (41, 5, '修改', '', 'BUTTON', '', '', '', 'sys:menu:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (42, 5, '删除', '', 'BUTTON', '', '', '', 'sys:menu:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (43, 6, '查询', '', 'BUTTON', '', '', '', 'sys:dict:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (44, 6, '新增', '', 'BUTTON', '', '', '', 'sys:dict:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (45, 6, '修改', '', 'BUTTON', '', '', '', 'sys:dict:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (46, 6, '删除', '', 'BUTTON', '', '', '', 'sys:dict:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (47, 7, '查询', '', 'BUTTON', '', '', '', 'sys:dict:data:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (48, 7, '新增', '', 'BUTTON', '', '', '', 'sys:dict:data:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (49, 7, '修改', '', 'BUTTON', '', '', '', 'sys:dict:data:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (50, 7, '删除', '', 'BUTTON', '', '', '', 'sys:dict:data:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (51, 11, '查询', '', 'BUTTON', '', '', '', 'sys:post:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (52, 11, '新增', '', 'BUTTON', '', '', '', 'sys:post:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (53, 11, '修改', '', 'BUTTON', '', '', '', 'sys:post:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (54, 11, '删除', '', 'BUTTON', '', '', '', 'sys:post:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (55, 12, '新增', '', 'BUTTON', '', '', '', 'sys:config:create', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (56, 12, '刷新', '', 'BUTTON', '', '', '', 'sys:config:refresh', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (57, 12, '修改', '', 'BUTTON', '', '', '', 'sys:config:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (58, 12, '删除', '', 'BUTTON', '', '', '', 'sys:config:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (59, 13, '查询', '', 'BUTTON', '', '', '', 'sys:notice:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (60, 13, '新增', '', 'BUTTON', '', '', '', 'sys:notice:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (61, 13, '修改', '', 'BUTTON', '', '', '', 'sys:notice:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (62, 13, '删除', '', 'BUTTON', '', '', '', 'sys:notice:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (63, 13, '发布', '', 'BUTTON', '', '', '', 'sys:notice:publish', '', '', 0, 1, '', 5, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (64, 13, '撤回', '', 'BUTTON', '', '', '', 'sys:notice:revoke', '', '', 0, 1, '', 6, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (65, 14, '查询', '', 'BUTTON', '', '', '', 'sys:online:user:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (66, 14, '强制下线', '', 'BUTTON', '', '', '', 'sys:online:user:offline', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (67, 15, '查询', '', 'BUTTON', '', '', '', 'sys:log:op:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (68, 15, '删除', '', 'BUTTON', '', '', '', 'sys:log:op:delete', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (69, 16, '查询', '', 'BUTTON', '', '', '', 'sys:log:login:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (70, 16, '删除', '', 'BUTTON', '', '', '', 'sys:log:login:delete', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (71, 19, '验证码', '', 'MENU', 'Code', 'code', 'tool/code/index', '', 'code', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (72, 71, '查询', '', 'BUTTON', '', '', '', 'sys:verify:code:list', '', '', 0, 1, '', 1, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (73, 71, '新增', '', 'BUTTON', '', '', '', 'sys:verify:code:create', '', '', 0, 1, '', 2, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (74, 71, '修改', '', 'BUTTON', '', '', '', 'sys:verify:code:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (75, 71, '删除', '', 'BUTTON', '', '', '', 'sys:verify:code:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (76, 15, '异常查询', '', 'BUTTON', '', '', '', 'sys:log:ex:list', '', '', 0, 1, '', 3, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (77, 15, '异常删除', '', 'BUTTON', '', '', '', 'sys:log:ex:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 14:30:00', '2026-08-06 14:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (78, 10, '定时任务', '', 'MENU', 'Job', 'job', 'system/job/index', '', 'todo', '', 0, 1, '', 6, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (79, 78, '查询', '', 'BUTTON', '', '', '', 'sys:job:list', '', '', 0, 1, '', 1, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (80, 78, '新增', '', 'BUTTON', '', '', '', 'sys:job:create', '', '', 0, 1, '', 2, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (81, 78, '修改', '', 'BUTTON', '', '', '', 'sys:job:modify', '', '', 0, 1, '', 3, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (82, 78, '删除', '', 'BUTTON', '', '', '', 'sys:job:delete', '', '', 0, 1, '', 4, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (83, 78, '立即执行', '', 'BUTTON', '', '', '', 'sys:job:run', '', '', 0, 1, '', 5, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (84, 10, '定时任务日志', '', 'MENU', 'JobLog', 'job-log', 'system/jobLog/index', '', 'document', '', 0, 1, '', 7, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (85, 84, '查询', '', 'BUTTON', '', '', '', 'sys:job:log:list', '', '', 0, 1, '', 1, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (86, 84, '删除', '', 'BUTTON', '', '', '', 'sys:job:log:delete', '', '', 0, 1, '', 2, 1, '2026-08-06 15:30:00', '2026-08-06 15:30:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (200, 0, '开发工具', 'Develop', 'DIRECTORY', '', '/develop', 'Layout', '', 'api', '/develop/codegen', 0, 1, '', 10, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (201, 200, '代码生成', 'Codegen', 'MENU', 'Codegen', 'codegen', 'codegen/index', '', 'java', '', 0, 1, '', 1, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (202, 200, '数据库管理', 'Database', 'MENU', 'Database', 'database', 'tool/database/index', '', 'table', '', 0, 1, '', 2, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (203, 200, 'SQL控制台', 'SqlConsole', 'MENU', 'SqlConsole', 'sql-console', 'tool/database/sql', '', 'code', '', 0, 1, '', 3, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (210, 202, '查询结构', '', 'BUTTON', '', '', '', 'tool:db:select', '', '', 0, 1, '', 1, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (211, 202, '数据新增', '', 'BUTTON', '', '', '', 'tool:db:create', '', '', 0, 1, '', 2, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (212, 202, '数据修改', '', 'BUTTON', '', '', '', 'tool:db:modify', '', '', 0, 1, '', 3, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (213, 202, '数据删除', '', 'BUTTON', '', '', '', 'tool:db:delete', '', '', 0, 1, '', 4, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (214, 203, '执行SQL', '', 'BUTTON', '', '', '', 'tool:db:execute', '', '', 0, 1, '', 1, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (215, 203, '执行DDL', '', 'BUTTON', '', '', '', 'tool:db:ddl', '', '', 0, 1, '', 2, 1, '2026-08-10 10:00:00', '2026-08-10 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (204, 200, '缓存管理', 'Cache', 'MENU', 'Cache', 'cache', 'tool/cache/index', '', 'redis', '', 0, 1, '', 4, 1, '2026-08-12 10:00:00', '2026-08-12 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (205, 204, '缓存查询', '', 'BUTTON', '', '', '', 'tool:cache:select', '', '', 0, 1, '', 1, 1, '2026-08-12 10:00:00', '2026-08-12 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (206, 204, '缓存修改', '', 'BUTTON', '', '', '', 'tool:cache:modify', '', '', 0, 1, '', 2, 1, '2026-08-12 10:00:00', '2026-08-12 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (207, 204, '缓存删除', '', 'BUTTON', '', '', '', 'tool:cache:delete', '', '', 0, 1, '', 3, 1, '2026-08-12 10:00:00', '2026-08-12 10:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (216, 10, '限流与封IP', 'RateLimit', 'MENU', 'RateLimit', 'rate-limit', 'monitor/rateLimit/index', '', 'ip', '', 0, 1, '', 8, 1, '2026-08-12 13:00:00', '2026-08-12 13:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (217, 216, '限流配置', '', 'BUTTON', '', '', '', 'sys:rate:limit:config', '', '', 0, 1, '', 1, 1, '2026-08-12 13:00:00', '2026-08-12 13:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (218, 216, '封禁IP查询', '', 'BUTTON', '', '', '', 'sys:rate:limit:list', '', '', 0, 1, '', 2, 1, '2026-08-12 13:00:00', '2026-08-12 13:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (219, 216, 'IP解封', '', 'BUTTON', '', '', '', 'sys:rate:limit:unban', '', '', 0, 1, '', 3, 1, '2026-08-12 13:00:00', '2026-08-12 13:00:00', 1);
INSERT INTO `sys_menu` (`id`, `parent_id`, `menu_name`, `menu_name_en`, `type`, `route_name`, `route_path`, `component`, `permission`, `icon`, `redirect`, `always_show`, `keep_alive`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (220, 216, 'IP手动封禁', '', 'BUTTON', '', '', '', 'sys:rate:limit:ban', '', '', 0, 1, '', 4, 1, '2026-08-12 13:00:00', '2026-08-12 13:00:00', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '用户id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4 COMMENT ='用户角色关联';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` (`id`, `user_id`, `role_id`, `create_time`)
VALUES (1, 1, 1, '2026-08-06 14:20:00');

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
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

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `role_id`     bigint(20)  NOT NULL COMMENT '角色id',
    `menu_id`     bigint(20)  NOT NULL COMMENT '菜单id',
    `create_time` datetime(0) NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 110
  DEFAULT CHARSET = utf8mb4 COMMENT ='角色菜单关联';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (100, 1, 1, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (101, 1, 2, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (102, 1, 26, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (103, 1, 8, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (104, 1, 27, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (105, 1, 28, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (106, 1, 29, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (107, 1, 3, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (108, 1, 30, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (109, 1, 31, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (110, 1, 32, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (111, 1, 33, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (112, 1, 4, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (113, 1, 34, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (114, 1, 35, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (115, 1, 36, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (116, 1, 37, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (117, 1, 38, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (118, 1, 5, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (119, 1, 39, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (120, 1, 40, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (121, 1, 41, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (122, 1, 42, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (123, 1, 6, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (124, 1, 43, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (125, 1, 44, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (126, 1, 45, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (127, 1, 46, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (128, 1, 7, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (129, 1, 47, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (130, 1, 48, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (131, 1, 49, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (132, 1, 50, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (133, 1, 11, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (134, 1, 51, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (135, 1, 52, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (136, 1, 53, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (137, 1, 54, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (138, 1, 12, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (139, 1, 55, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (140, 1, 56, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (141, 1, 57, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (142, 1, 58, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (143, 1, 13, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (144, 1, 59, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (145, 1, 60, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (146, 1, 61, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (147, 1, 62, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (148, 1, 63, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (149, 1, 64, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (150, 1, 19, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (151, 1, 20, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (152, 1, 21, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (153, 1, 22, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (154, 1, 23, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (155, 1, 24, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (156, 1, 25, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (157, 1, 71, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (158, 1, 72, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (159, 1, 73, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (160, 1, 74, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (161, 1, 75, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (162, 1, 10, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (163, 1, 14, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (164, 1, 65, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (165, 1, 66, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (166, 1, 15, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (167, 1, 67, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (168, 1, 68, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (169, 1, 76, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (170, 1, 77, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (171, 1, 16, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (172, 1, 69, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (173, 1, 70, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (174, 1, 17, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (175, 1, 18, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (176, 1, 78, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (177, 1, 79, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (178, 1, 80, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (179, 1, 81, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (180, 1, 82, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (181, 1, 83, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (182, 1, 84, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (183, 1, 85, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (184, 1, 86, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (185, 1, 200, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (186, 1, 201, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (187, 1, 202, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (188, 1, 210, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (189, 1, 211, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (190, 1, 212, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (191, 1, 213, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (192, 1, 203, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (193, 1, 214, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (194, 1, 215, '2026-08-10 17:55:36');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (195, 1, 204, '2026-08-12 06:59:41');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (196, 1, 205, '2026-08-12 06:59:41');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (197, 1, 206, '2026-08-12 06:59:41');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (198, 1, 207, '2026-08-12 06:59:41');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (199, 1, 216, '2026-08-12 13:00:00');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (200, 1, 217, '2026-08-12 13:00:00');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (201, 1, 218, '2026-08-12 13:00:00');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (202, 1, 219, '2026-08-12 13:00:00');
INSERT INTO `sys_role_menu` (`id`, `role_id`, `menu_id`, `create_time`) VALUES (203, 1, 220, '2026-08-12 13:00:00');

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
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

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` (`id`, `dict_code`, `dict_name`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'gender', '性别', '', 99, 1, '2024-10-31 18:00:21', '2024-10-31 18:00:21', 1);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
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
  AUTO_INCREMENT = 4
  DEFAULT CHARSET = utf8mb4 COMMENT ='字典数据';

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` (`id`, `dict_code`, `label`, `value`, `tag`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'gender', '男', '1', 'success', '', 1, 1, '2024-10-31 20:23:38', '2024-10-31 20:23:38', 1);
INSERT INTO `sys_dict_data` (`id`, `dict_code`, `label`, `value`, `tag`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (2, 'gender', '女', '2', 'primary', '', 1, 1, '2024-10-31 20:36:13', '2024-10-31 20:36:13', 1);
INSERT INTO `sys_dict_data` (`id`, `dict_code`, `label`, `value`, `tag`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (3, 'gender', '保密', '0', 'info', '', 3, 1, '2024-10-31 20:36:32', '2024-10-31 20:50:24', 0);
INSERT INTO `sys_dict_data` (`id`, `dict_code`, `label`, `value`, `tag`, `remark`, `sort`, `enabled`, `create_time`, `update_time`, `deleted`)
VALUES (4, 'gender', '保密', '0', 'info', '', 1, 1, '2024-10-31 20:23:38', '2024-10-31 20:23:38', 1);

-- ----------------------------
-- Table structure for sys_post
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
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

-- ----------------------------
-- Records of sys_post
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
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

-- ----------------------------
-- Records of sys_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_online_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_online_user`;
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

-- ----------------------------
-- Records of sys_online_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
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

-- ----------------------------
-- Records of sys_notice
-- ----------------------------

-- ----------------------------
-- Table structure for sys_notice_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_user`;
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

-- ----------------------------
-- Records of sys_notice_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_op
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_op`;
CREATE TABLE `sys_log_op`
(
    `id`          bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)  NOT NULL COMMENT '操作用户id',
    `operator`    varchar(32)  DEFAULT '' COMMENT '操作员',
    `module`      varchar(32)  DEFAULT '' COMMENT '模块',
    `request_uri` varchar(255) DEFAULT '' COMMENT '请求路径',
    `method`      varchar(128) DEFAULT '' COMMENT '请求方法',
    `params`      varchar(255) DEFAULT '' COMMENT '请求参数',
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

-- ----------------------------
-- Records of sys_log_op
-- ----------------------------

-- ----------------------------
-- Table structure for sys_log_login
-- ----------------------------
DROP TABLE IF EXISTS `sys_log_login`;
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

-- ----------------------------
-- Records of sys_log_login
-- ----------------------------

-- ----------------------------
-- Table structure for sys_file_manage
-- ----------------------------
DROP TABLE IF EXISTS `sys_file_manage`;
CREATE TABLE `sys_file_manage`
(
    `id`          bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`     bigint(20)   NOT NULL COMMENT '上传者id',
    `name`        varchar(255) NOT NULL COMMENT '文件名',
    `md5`         varchar(32)  NOT NULL COMMENT 'MD5',
    `size`        bigint(20)   NOT NULL COMMENT '文件大小',
    `suffix`      varchar(32)  DEFAULT '' COMMENT '文件后缀',
    `platform`    varchar(32)  DEFAULT '' COMMENT '上传平台',
    `url`         varchar(255) DEFAULT '' COMMENT '文件路径',
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

-- ----------------------------
-- Records of sys_file_manage
-- ----------------------------

-- ----------------------------
-- Table structure for sys_verify_code
-- ----------------------------
DROP TABLE IF EXISTS `sys_verify_code`;
CREATE TABLE `sys_verify_code`
(
    `id`           bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `user_id`      bigint(20) COMMENT '用户id',
    `contact`      varchar(32) NOT NULL COMMENT '联系方式',
    `contact_type` tinyint(2)  NOT NULL COMMENT '联系类型',
    `verify_code`  varchar(16) NOT NULL COMMENT '验证码',
    `verify_type`  tinyint(2)  NOT NULL COMMENT '验证类型',
    `platform`     tinyint(2)  NOT NULL COMMENT '平台',
    `expire_time`  datetime(0) NOT NULL COMMENT '过期时间',
    `remark`       varchar(255) DEFAULT '' COMMENT '备注',
    `sort`         int          DEFAULT '0' COMMENT '排序',
    `enabled`      tinyint(2)   DEFAULT '1' COMMENT '是否启用',
    `create_time`  datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`  datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`      tinyint(2)   DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='验证码';

-- ----------------------------
-- Records of sys_verify_code
-- ----------------------------

-- ----------------------------
-- Table structure for sys_gen_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_config`;
CREATE TABLE `sys_gen_config`
(
    `id`                bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `table_name`        varchar(64)  NOT NULL COMMENT '表名',
    `business_name`     varchar(32)  DEFAULT '' COMMENT '业务名',
    `module_name`       varchar(32)  DEFAULT '' COMMENT '模块名',
    `package_name`      varchar(100) DEFAULT '' COMMENT '包名',
    `entity_name`       varchar(64)  DEFAULT '' COMMENT '实体名',
    `author`            varchar(32)  DEFAULT '' COMMENT '作者',
    `parent_menu_id`    bigint(20)   DEFAULT NULL COMMENT '上级菜单id',
    `backend_app_name`  varchar(64)  DEFAULT '' COMMENT '后端应用名',
    `frontend_app_name` varchar(64)  DEFAULT '' COMMENT '前端应用名',
    `remark`            varchar(255) DEFAULT '' COMMENT '描述',
    `sort`              int          DEFAULT '0' COMMENT '排序',
    `enabled`           int          DEFAULT '1' COMMENT '是否启用：0禁用，1启用',
    `create_time`       datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`       datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`           int          DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_gen_config_table_name` (`table_name`, `deleted`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='代码生成配置表';

-- ----------------------------
-- Records of sys_gen_config
-- ----------------------------

-- ----------------------------
-- Table structure for sys_gen_field_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_gen_field_config`;
CREATE TABLE `sys_gen_field_config`
(
    `id`              bigint(20)  NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `config_id`       bigint(20)  NOT NULL COMMENT '生成配置id',
    `column_name`     varchar(64) NOT NULL COMMENT '列名',
    `column_type`     varchar(64) DEFAULT '' COMMENT '列类型',
    `field_name`      varchar(64) DEFAULT '' COMMENT '字段名',
    `field_type`      varchar(32) DEFAULT '' COMMENT '字段类型',
    `field_comment`   varchar(255) DEFAULT '' COMMENT '字段注释',
    `is_show_in_list` int         DEFAULT '1' COMMENT '是否列表显示：0否，1是',
    `is_show_in_form` int         DEFAULT '1' COMMENT '是否表单显示：0否，1是',
    `is_show_in_query` int        DEFAULT '0' COMMENT '是否查询条件：0否，1是',
    `is_required`     int         DEFAULT '0' COMMENT '是否必填：0否，1是',
    `form_type`       int         DEFAULT '1' COMMENT '表单类型',
    `query_type`      int         DEFAULT '1' COMMENT '查询类型',
    `max_length`      int         DEFAULT '0' COMMENT '最大长度',
    `field_sort`      int         DEFAULT '0' COMMENT '字段排序',
    `dict_type`       varchar(64) DEFAULT '' COMMENT '字典类型',
    `create_time`     datetime(0) NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0) NOT NULL COMMENT '修改时间',
    `deleted`         int         DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_gen_field_config_id` (`config_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='代码生成字段配置表';

-- ----------------------------
-- Records of sys_gen_field_config
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
