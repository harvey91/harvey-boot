CREATE TABLE `sys_job`
(
    `id`              bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `job_name`        varchar(64)  DEFAULT '' COMMENT '任务名称',
    `job_group`       varchar(64)  DEFAULT 'DEFAULT' COMMENT '任务组名',
    `invoke_target`   varchar(500) NOT NULL COMMENT '调用目标字符串',
    `cron_expression` varchar(255) DEFAULT '' COMMENT 'cron执行表达式',
    `misfire_policy`  tinyint(2)   DEFAULT '3' COMMENT '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
    `concurrent`      tinyint(2)   DEFAULT '1' COMMENT '是否并发执行（0允许 1禁止）',
    `remark`          varchar(500) DEFAULT '' COMMENT '备注',
    `enabled`         tinyint(2)   DEFAULT '1' COMMENT '是否启用',
    `create_time`     datetime(0)  NOT NULL COMMENT '创建时间',
    `update_time`     datetime(0)  NOT NULL COMMENT '修改时间',
    `deleted`         tinyint(2)   DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='定时任务调度';

CREATE TABLE `sys_job_log`
(
    `id`             bigint(20)   NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `job_name`       varchar(64)  NOT NULL COMMENT '任务名称',
    `job_group`      varchar(64)  NOT NULL COMMENT '任务组名',
    `invoke_target`  varchar(500) NOT NULL COMMENT '调用目标字符串',
    `job_message`    varchar(500)  DEFAULT '' COMMENT '日志信息',
    `exception_info` varchar(2000) DEFAULT '' COMMENT '异常信息',
    `enabled`        tinyint(2)    DEFAULT '1' COMMENT '是否启用',
    `create_time`    datetime(0)  NOT NULL COMMENT '创建时间',
    `deleted`        tinyint(2)    DEFAULT '1' COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4 COMMENT ='定时任务调度日志';