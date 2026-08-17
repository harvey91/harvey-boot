-- ----------------------------
-- AI 模型配置表
-- ----------------------------
DROP TABLE IF EXISTS `ai_model_config`;
CREATE TABLE `ai_model_config` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `model_name` varchar(64) NOT NULL COMMENT '模型展示名称',
  `provider` varchar(32) NOT NULL DEFAULT 'deepseek' COMMENT '厂商: deepseek/qwen/glm/kimi/ollama/other',
  `model` varchar(64) NOT NULL COMMENT '模型标识, 如 deepseek-chat',
  `base_url` varchar(255) NOT NULL COMMENT '接口地址',
  `api_key` varchar(255) DEFAULT NULL COMMENT 'API密钥(AES加密存储)',
  `is_default` tinyint NOT NULL DEFAULT 0 COMMENT '是否默认模型(0否,1是)',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用(0禁用,1启用)',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除(1未删除,0已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_enabled_default` (`enabled`, `is_default`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='AI模型配置表';

-- ----------------------------
-- 初始数据
-- ----------------------------
INSERT INTO `ai_model_config` (`id`, `model_name`, `provider`, `model`, `base_url`, `api_key`, `is_default`, `enabled`, `sort`, `remark`, `create_time`, `update_time`, `deleted`)
VALUES (1, 'DeepSeek', 'deepseek', 'deepseek-chat', 'https://api.deepseek.com', NULL, 1, 1, 1, 'DeepSeek 官方接口', NOW(), NOW(), 1);
INSERT INTO `ai_model_config` (`id`, `model_name`, `provider`, `model`, `base_url`, `api_key`, `is_default`, `enabled`, `sort`, `remark`, `create_time`, `update_time`, `deleted`)
VALUES (2, '通义千问', 'qwen', 'qwen-plus', 'https://dashscope.aliyuncs.com/compatible-mode/v1', NULL, 0, 1, 2, '阿里云百炼 DashScope OpenAI兼容', NOW(), NOW(), 1);
INSERT INTO `ai_model_config` (`id`, `model_name`, `provider`, `model`, `base_url`, `api_key`, `is_default`, `enabled`, `sort`, `remark`, `create_time`, `update_time`, `deleted`)
VALUES (3, '本地 Ollama', 'ollama', 'qwen2.5:7b', 'http://localhost:11434/v1', NULL, 0, 0, 3, '本地部署, 无需 API Key', NOW(), NOW(), 1);
-- ----------------------------
-- 知识库表
-- ----------------------------
DROP TABLE IF EXISTS `ai_knowledge_base`;
CREATE TABLE `ai_knowledge_base` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '知识库名称',
  `description` varchar(500) DEFAULT NULL COMMENT '知识库描述',
  `doc_count` int NOT NULL DEFAULT 0 COMMENT '文档数量',
  `chunk_count` int NOT NULL DEFAULT 0 COMMENT '分块数量',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用(0禁用,1启用)',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除(1未删除,0已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI知识库表';

-- ----------------------------
-- 知识库文档表
-- ----------------------------
DROP TABLE IF EXISTS `ai_doc`;
CREATE TABLE `ai_doc` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `kb_id` bigint NOT NULL COMMENT '知识库ID',
  `file_name` varchar(255) NOT NULL COMMENT '文件名',
  `file_type` varchar(32) DEFAULT NULL COMMENT '文件类型, 如 txt/md/pdf/docx',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小(字节)',
  `file_url` varchar(255) DEFAULT NULL COMMENT '文件存储地址',
  `chunk_count` int NOT NULL DEFAULT 0 COMMENT '分块数量',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '解析状态(0待解析,1解析中,2就绪,3失败)',
  `parse_time` datetime DEFAULT NULL COMMENT '解析完成时间',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用(0禁用,1启用)',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除(1未删除,0已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_kb_id` (`kb_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI知识库文档表';

-- ----------------------------
-- 知识库文档分块表
-- ----------------------------
DROP TABLE IF EXISTS `ai_doc_chunk`;
CREATE TABLE `ai_doc_chunk` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `kb_id` bigint NOT NULL COMMENT '知识库ID',
  `doc_id` bigint NOT NULL COMMENT '文档ID',
  `chunk_index` int NOT NULL DEFAULT 0 COMMENT '分块序号(从0开始)',
  `content` text NOT NULL COMMENT '分块内容',
  `char_count` int NOT NULL DEFAULT 0 COMMENT '字符数',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用(0禁用,1启用)',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除(1未删除,0已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_kb_id` (`kb_id`),
  KEY `idx_doc_id` (`doc_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI知识库文档分块表';

-- ----------------------------
-- 常见问题表(智能客服)
-- ----------------------------
DROP TABLE IF EXISTS `ai_faq`;
CREATE TABLE `ai_faq` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `question` varchar(500) NOT NULL COMMENT '问题',
  `answer` text NOT NULL COMMENT '标准答案',
  `category` varchar(64) DEFAULT '' COMMENT '分类',
  `hit_count` int NOT NULL DEFAULT 0 COMMENT '命中次数',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用(0禁用,1启用)',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除(1未删除,0已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_enabled` (`enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI客服常见问题表';

-- ----------------------------
-- 客服工单表
-- ----------------------------
DROP TABLE IF EXISTS `ai_work_order`;
CREATE TABLE `ai_work_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `conversation_id` varchar(64) DEFAULT NULL COMMENT '会话ID',
  `contact` varchar(64) DEFAULT NULL COMMENT '联系方式',
  `content` text NOT NULL COMMENT '客户问题',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '状态(0待处理,1已回复,2已关闭)',
  `reply` text DEFAULT NULL COMMENT '人工回复',
  `reply_time` datetime DEFAULT NULL COMMENT '回复时间',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `sort` int NOT NULL DEFAULT 99 COMMENT '排序',
  `enabled` tinyint NOT NULL DEFAULT 1 COMMENT '是否启用(0禁用,1启用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NOT NULL DEFAULT 1 COMMENT '逻辑删除(1未删除,0已删除)',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI客服工单表';
