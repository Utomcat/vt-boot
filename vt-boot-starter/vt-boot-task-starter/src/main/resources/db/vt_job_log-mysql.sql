-- 任务执行日志表(MySQL)
CREATE TABLE IF NOT EXISTS vt_job_log
(
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    job_name     VARCHAR(128) NOT NULL COMMENT '任务名称',
    job_group    VARCHAR(64)  NOT NULL DEFAULT 'DEFAULT' COMMENT '任务分组',
    trigger_name VARCHAR(128)          DEFAULT NULL COMMENT '触发器名称',
    fire_time    DATETIME(3)           DEFAULT NULL COMMENT '计划触发时间',
    start_time   DATETIME(3)           DEFAULT NULL COMMENT '实际开始时间',
    end_time     DATETIME(3)           DEFAULT NULL COMMENT '结束时间',
    duration_ms  BIGINT                DEFAULT NULL COMMENT '耗时(毫秒)',
    status       TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-执行中 1-成功 2-失败 3-被否决',
    params       TEXT                  DEFAULT NULL COMMENT '任务参数(JSON)',
    error_msg    TEXT                  DEFAULT NULL COMMENT '异常信息',
    instance_id  VARCHAR(64)           DEFAULT NULL COMMENT '调度器实例标识',
    tenant_id    VARCHAR(64)           DEFAULT NULL COMMENT '租户ID',
    create_time  DATETIME(3)  NOT NULL COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_job_log_job_fire (job_name, fire_time),
    KEY idx_job_log_create_time (create_time)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='任务执行日志';
