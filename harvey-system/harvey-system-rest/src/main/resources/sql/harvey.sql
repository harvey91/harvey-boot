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
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，部门id',
  `parent_id` bigint NOT NULL COMMENT '父id',
  `dept_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门编号',
  `dept_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '部门名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '部门描述',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted` int NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (5, 0, '1', '总公司', '', 99, 1, '2024-10-30 17:27:34', '2024-10-30 17:27:34', 1);
INSERT INTO `sys_dept` VALUES (7, 0, '', '分公司1', '', 99, 1, '2024-10-30 17:27:53', '2024-10-30 17:27:53', 1);
INSERT INTO `sys_dept` VALUES (8, 5, '', '研发部', '', 99, 1, '2024-10-30 17:28:02', '2024-10-30 17:28:02', 1);

-- ----------------------------
-- Table structure for sys_dept_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_dept_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dept_id` bigint NOT NULL COMMENT '部门id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色部门关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept_role
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，角色id',
  `dict_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典编码',
  `dict_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典名称',
  `remark` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '角色描述',
  `sort` int NULL DEFAULT 99 COMMENT '排序',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted` int NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'gender', '性别', '', 99, 1, '2024-10-31 18:00:21', '2024-10-31 18:00:21', 1);

-- ----------------------------
-- Table structure for sys_dict_data
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，角色id',
  `dict_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典编码',
  `label` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典项',
  `value` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字典值',
  `tag` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '前端标签tag值',
  `sort` int NULL DEFAULT 99 COMMENT '排序',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted` int NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统字典数据表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_data
-- ----------------------------
INSERT INTO `sys_dict_data` VALUES (1, 'gender', '男', '1', 'success', 1, 1, '2024-10-31 20:23:38', '2024-10-31 20:23:38', 1);
INSERT INTO `sys_dict_data` VALUES (2, 'gender', '女', '2', 'primary', 1, 1, '2024-10-31 20:36:13', '2024-10-31 20:36:13', 1);
INSERT INTO `sys_dict_data` VALUES (3, 'gender', '保密', '0', 'info', 3, 1, '2024-10-31 20:36:32', '2024-10-31 20:50:24', 0);
INSERT INTO `sys_dict_data` VALUES (4, 'gender', '保密', '0', 'info', 1, 1, '2024-10-31 20:23:38', '2024-10-31 20:23:38', 1);

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，菜单id',
  `parent_id` bigint NOT NULL COMMENT '上级id',
  `menu_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `menu_name_en` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单英文名称',
  `type` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '菜单类型：DIRECTORY目录，MENU菜单，BUTTON按钮，LINK链接',
  `route_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由名称',
  `route_path` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '路由路径',
  `component` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '组件路径',
  `permission` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '权限标识',
  `icon` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '图标',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '重定向地址',
  `always_show` int NULL DEFAULT 0 COMMENT '始终显示',
  `keep_alive` int NULL DEFAULT 1 COMMENT '缓存页面',
  `sort` int NULL DEFAULT 99 COMMENT '排序',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted` int NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '系统管理', 'System', 'DIRECTORY', '', '/system', 'Layout', '', 'system', '/system/user', 0, 1, 2, 1, '2024-10-31 10:44:59', '2024-10-31 10:45:02', 1);
INSERT INTO `sys_menu` VALUES (2, 1, '用户管理', 'User', 'MENU', 'User', 'user', 'system/user/index', '', 'el-icon-User', '', 0, 1, 1, 1, '2024-10-31 10:46:43', '2024-10-31 10:46:56', 1);
INSERT INTO `sys_menu` VALUES (3, 1, '部门管理', 'Dept', 'MENU', 'Dept', 'dept', 'system/dept/index', '', 'role', '', 0, 1, 2, 1, '2024-10-31 10:46:47', '2024-10-31 10:46:58', 1);
INSERT INTO `sys_menu` VALUES (4, 1, '角色管理', 'Role', 'MENU', 'Role', 'role', 'system/role/index', '', 'tree', '', 0, 1, 3, 1, '2024-10-31 10:46:49', '2024-10-31 10:47:00', 1);
INSERT INTO `sys_menu` VALUES (5, 1, '菜单管理', 'Menu', 'MENU', 'Menu', 'menu', 'system/menu/index', '', 'menu', '', 0, 1, 4, 1, '2024-10-31 10:46:54', '2024-10-31 10:47:01', 1);
INSERT INTO `sys_menu` VALUES (6, 1, '字典管理', '', 'MENU', 'Dict', 'dict', 'system/dict/index', '', 'dict', '', 0, 1, 5, 1, '2024-10-31 17:21:12', '2024-10-31 17:21:12', 1);
INSERT INTO `sys_menu` VALUES (7, 1, '字典数据', '', 'MENU', 'DictData', 'dict-data', 'system/dict/data', '', 'dict', '', 0, 1, 6, 0, '2024-10-31 19:57:53', '2024-10-31 19:57:53', 1);
INSERT INTO `sys_menu` VALUES (8, 2, '新增用户', '', 'BUTTON', '', '', '', 'sys:user:add', '', '', 0, 1, 1, 1, '2024-11-06 21:02:41', '2024-11-06 21:02:41', 1);
INSERT INTO `sys_menu` VALUES (9, 0, '组织架构', '', 'DIRECTORY', '', '/organization', 'Layout', '', 'cascader', '/system/user', 0, 1, 1, 1, '2024-11-06 21:24:21', '2024-11-06 21:24:21', 1);
INSERT INTO `sys_menu` VALUES (10, 0, '服务监控', '', 'DIRECTORY', '', '/monitor', 'Layout', '', 'client', '/monitor/server', 0, 1, 9, 1, '2024-11-06 21:33:55', '2024-11-06 21:33:55', 1);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，角色id',
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `data_scope` int NOT NULL DEFAULT 3 COMMENT '数据权限：0全部数据，1部门及子部门数据，2本部门数据，3本人数据',
  `remark` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '角色描述',
  `sort` int NULL DEFAULT 99 COMMENT '排序',
  `enabled` int NOT NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted` int NOT NULL DEFAULT 1 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 'admin', '管理员', 0, '', 1, 1, '2024-10-31 08:37:30', '2024-10-31 08:37:30', 1);
INSERT INTO `sys_role` VALUES (2, 'dev', '开发人员', 1, '', 2, 1, '2024-10-31 08:39:02', '2024-10-31 08:39:02', 1);
INSERT INTO `sys_role` VALUES (3, '21', '产品经理', 2, '', 3, 1, '2024-10-31 13:24:19', '2024-10-31 13:24:19', 1);
INSERT INTO `sys_role` VALUES (4, 'em', '普通员工', 3, '', 4, 1, '2024-11-05 09:48:46', '2024-11-05 09:48:46', 1);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `menu_id` bigint NOT NULL COMMENT '菜单id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `un_menu_role`(`role_id`, `menu_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (14, 1, 1, '2024-11-06 20:25:52');
INSERT INTO `sys_role_menu` VALUES (15, 1, 2, '2024-11-06 20:25:52');
INSERT INTO `sys_role_menu` VALUES (16, 1, 3, '2024-11-06 20:25:52');
INSERT INTO `sys_role_menu` VALUES (17, 1, 4, '2024-11-06 20:25:52');
INSERT INTO `sys_role_menu` VALUES (18, 1, 5, '2024-11-06 20:25:52');
INSERT INTO `sys_role_menu` VALUES (19, 1, 6, '2024-11-06 20:25:52');
INSERT INTO `sys_role_menu` VALUES (20, 1, 7, '2024-11-06 20:25:52');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键，用户id',
  `username` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `nickname` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '头像',
  `gender` int NULL DEFAULT 0 COMMENT '性别：0未知，1男，2女',
  `email` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '邮箱',
  `phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '手机号',
  `dept_id` bigint NULL DEFAULT 0 COMMENT '所属部门id',
  `last_login_ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '最后登录IP',
  `last_login_time` datetime(0) NULL DEFAULT NULL COMMENT '最后登录时间',
  `enabled` int NULL DEFAULT 1 COMMENT '是否启用：0禁用，1启用',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '修改时间',
  `deleted` int NULL DEFAULT 1 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$3eQN7NGT1e0wLr.RQGxsVuak2JyWz/0Ky.WVOVUsAd7/t5muiviEu', '管理员', 'https://gitee.com/harvey-enterprise-level/harvey-base/raw/master/harvey-system/src/main/resources/static/avatar.png', 1, '11@qq.com', '18508945529', 5, '', '2024-10-30 22:14:33', 1, '2024-10-30 22:14:37', '2024-11-04 23:29:11', 1);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `role_id` bigint NOT NULL COMMENT '角色id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
