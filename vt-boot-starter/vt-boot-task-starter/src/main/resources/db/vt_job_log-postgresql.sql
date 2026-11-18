-- 任务执行日志表(PostgreSQL)
CREATE TABLE IF NOT EXISTS vt_job_log
(
    id           BIGSERIAL    NOT NULL PRIMARY KEY,
    job_name     VARCHAR(128) NOT NULL,
    job_group    VARCHAR(64)  NOT NULL DEFAULT 'DEFAULT',
    trigger_name VARCHAR(128)          DEFAULT NULL,
    fire_time    TIMESTAMP(3)          DEFAULT NULL,
    start_time   TIMESTAMP(3)          DEFAULT NULL,
    end_time     TIMESTAMP(3)          DEFAULT NULL,
    duration_ms  BIGINT                DEFAULT NULL,
    status       SMALLINT     NOT NULL DEFAULT 0,
    params       TEXT                  DEFAULT NULL,
    error_msg    TEXT                  DEFAULT NULL,
    instance_id  VARCHAR(64)           DEFAULT NULL,
    tenant_id    VARCHAR(64)           DEFAULT NULL,
    create_time  TIMESTAMP(3) NOT NULL
);

COMMENT ON TABLE vt_job_log IS '任务执行日志';
COMMENT ON COLUMN vt_job_log.status IS '状态: 0-执行中 1-成功 2-失败 3-被否决';

CREATE INDEX IF NOT EXISTS idx_job_log_job_fire ON vt_job_log (job_name, fire_time);
CREATE INDEX IF NOT EXISTS idx_job_log_create_time ON vt_job_log (create_time);
