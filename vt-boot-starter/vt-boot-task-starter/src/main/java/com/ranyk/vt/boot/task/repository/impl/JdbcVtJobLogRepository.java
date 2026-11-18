package com.ranyk.vt.boot.task.repository.impl;

import com.ranyk.vt.boot.task.constant.JobLogStatusEnum;
import com.ranyk.vt.boot.task.domain.po.VtJobLogPO;
import com.ranyk.vt.boot.task.properties.VtTaskLogProperties;
import com.ranyk.vt.boot.task.repository.VtJobLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * CLASS_NAME: JdbcVtJobLogRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 基于 JdbcTemplate 的任务执行日志仓储实现
 * <p>
 * 写入异常只打 warn 日志, 绝不向上抛出, 保证"日志记录失败不影响任务执行"。
 * </p>
 * @date: 2026-11-18
 */
@Slf4j
public class JdbcVtJobLogRepository implements VtJobLogRepository {

    /**
     * 合法表名匹配规则 - 只允许字母、数字、下划线和点, 防止表名拼接导致的 SQL 注入
     */
    private static final Pattern TABLE_NAME_PATTERN = Pattern.compile("^[A-Za-z0-9_.]+$");

    /**
     * 结果集映射器
     */
    private static final RowMapper<VtJobLogPO> ROW_MAPPER = (rs, rowNum) -> {
        VtJobLogPO po = new VtJobLogPO();
        po.setId(rs.getLong("id"));
        po.setJobName(rs.getString("job_name"));
        po.setJobGroup(rs.getString("job_group"));
        po.setTriggerName(rs.getString("trigger_name"));
        po.setFireTime(toLocalDateTime(rs, "fire_time"));
        po.setStartTime(toLocalDateTime(rs, "start_time"));
        po.setEndTime(toLocalDateTime(rs, "end_time"));
        po.setDurationMs(getLong(rs, "duration_ms"));
        po.setStatus(rs.getInt("status"));
        po.setParams(rs.getString("params"));
        po.setErrorMsg(rs.getString("error_msg"));
        po.setInstanceId(rs.getString("instance_id"));
        po.setTenantId(rs.getString("tenant_id"));
        po.setCreateTime(toLocalDateTime(rs, "create_time"));
        return po;
    };

    /**
     * JDBC 模板对象
     */
    private final JdbcTemplate jdbcTemplate;

    /**
     * 日志配置属性对象
     */
    private final VtTaskLogProperties logProperties;

    /**
     * 构造函数
     *
     * @param jdbcTemplate  JDBC 模板对象 {@link JdbcTemplate}
     * @param logProperties 日志配置属性对象 {@link VtTaskLogProperties}
     */
    public JdbcVtJobLogRepository(JdbcTemplate jdbcTemplate, VtTaskLogProperties logProperties) {
        this.jdbcTemplate = jdbcTemplate;
        this.logProperties = logProperties;
        String tableName = logProperties.getTableName();
        if (tableName == null || !TABLE_NAME_PATTERN.matcher(tableName).matches()) {
            throw new IllegalArgumentException("非法的任务日志表名: " + tableName);
        }
    }

    /**
     * 保存一条"运行中"日志
     *
     * @param log 日志对象 {@link VtJobLogPO}
     * @return 日志主键, 保存失败返回 null
     */
    @Override
    public Long saveStart(VtJobLogPO logPO) {
        String sql = "INSERT INTO " + tableName()
                + " (job_name, job_group, trigger_name, fire_time, start_time, status, params, instance_id, tenant_id, create_time)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
                ps.setString(1, logPO.getJobName());
                ps.setString(2, logPO.getJobGroup());
                ps.setString(3, logPO.getTriggerName());
                ps.setTimestamp(4, toTimestamp(logPO.getFireTime()));
                ps.setTimestamp(5, toTimestamp(logPO.getStartTime()));
                ps.setInt(6, logPO.getStatus() == null ? JobLogStatusEnum.RUNNING.getValue() : logPO.getStatus());
                ps.setString(7, logPO.getParams());
                ps.setString(8, logPO.getInstanceId());
                ps.setString(9, logPO.getTenantId());
                ps.setTimestamp(10, toTimestamp(logPO.getCreateTime() == null ? LocalDateTime.now() : logPO.getCreateTime()));
                return ps;
            }, keyHolder);
            Number key = keyHolder.getKey();
            return key == null ? null : key.longValue();
        } catch (Exception e) {
            log.warn("写入任务执行日志失败(不影响任务执行). job: {}", logPO.getJobName(), e);
            return null;
        }
    }

    /**
     * 标记日志为执行结束
     *
     * @param id         日志主键
     * @param status     结束状态, 取值见 JobLogStatusEnum
     * @param durationMs 耗时(毫秒)
     * @param errorMsg   异常信息, 无异常传 null
     */
    @Override
    public void markFinished(Long id, Integer status, Long durationMs, String errorMsg) {
        if (id == null) {
            return;
        }
        String sql = "UPDATE " + tableName() + " SET status = ?, end_time = ?, duration_ms = ?, error_msg = ? WHERE id = ?";
        try {
            jdbcTemplate.update(sql, status, Timestamp.valueOf(LocalDateTime.now()), durationMs,
                    truncate(errorMsg, logProperties.getMaxErrorLength()), id);
        } catch (Exception e) {
            log.warn("更新任务执行日志失败(不影响任务执行). id: {}", id, e);
        }
    }

    /**
     * 按主键查询日志
     *
     * @param id 日志主键
     * @return 日志对象
     */
    @Override
    public Optional<VtJobLogPO> findById(Long id) {
        List<VtJobLogPO> list = jdbcTemplate.query("SELECT * FROM " + tableName() + " WHERE id = ?", ROW_MAPPER, id);
        return list.stream().findFirst();
    }

    /**
     * 删除指定时间之前的日志
     *
     * @param before 时间点
     * @return 删除条数
     */
    @Override
    public int deleteBefore(LocalDateTime before) {
        return jdbcTemplate.update("DELETE FROM " + tableName() + " WHERE create_time < ?", Timestamp.valueOf(before));
    }

    /**
     * 分页查询日志
     *
     * @param jobName 任务名称, 可为空
     * @param status  状态, 可为空
     * @param from    开始时间, 可为空
     * @param to      结束时间, 可为空
     * @param offset  偏移量
     * @param limit   每页条数
     * @return 日志列表
     */
    @Override
    public List<VtJobLogPO> query(String jobName, Integer status, LocalDateTime from, LocalDateTime to, int offset, int limit) {
        List<Object> args = new ArrayList<>();
        String where = buildWhere(jobName, status, from, to, args);
        args.add(limit);
        args.add(offset);
        String sql = "SELECT * FROM " + tableName() + where + " ORDER BY id DESC LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, args.toArray());
    }

    /**
     * 统计日志条数
     *
     * @param jobName 任务名称, 可为空
     * @param status  状态, 可为空
     * @param from    开始时间, 可为空
     * @param to      结束时间, 可为空
     * @return 总条数
     */
    @Override
    public long count(String jobName, Integer status, LocalDateTime from, LocalDateTime to) {
        List<Object> args = new ArrayList<>();
        String where = buildWhere(jobName, status, from, to, args);
        Long total = jdbcTemplate.queryForObject("SELECT COUNT(1) FROM " + tableName() + where, Long.class, args.toArray());
        return total == null ? 0L : total;
    }

    /**
     * 构建查询条件
     *
     * @param jobName 任务名称, 可为空
     * @param status  状态, 可为空
     * @param from    开始时间, 可为空
     * @param to      结束时间, 可为空
     * @param args    参数收集列表
     * @return where 子句
     */
    private String buildWhere(String jobName, Integer status, LocalDateTime from, LocalDateTime to, List<Object> args) {
        StringBuilder where = new StringBuilder(" WHERE 1 = 1");
        if (jobName != null && !jobName.isBlank()) {
            where.append(" AND job_name = ?");
            args.add(jobName);
        }
        if (status != null) {
            where.append(" AND status = ?");
            args.add(status);
        }
        if (from != null) {
            where.append(" AND fire_time >= ?");
            args.add(Timestamp.valueOf(from));
        }
        if (to != null) {
            where.append(" AND fire_time <= ?");
            args.add(Timestamp.valueOf(to));
        }
        return where.toString();
    }

    /**
     * 获取日志表名
     *
     * @return 日志表名
     */
    private String tableName() {
        return logProperties.getTableName();
    }

    /**
     * 转换为 JDBC 时间戳
     *
     * @param dateTime 时间对象
     * @return JDBC 时间戳
     */
    private static Timestamp toTimestamp(LocalDateTime dateTime) {
        return dateTime == null ? null : Timestamp.valueOf(dateTime);
    }

    /**
     * 从结果集读取时间列
     *
     * @param rs         结果集
     * @param columnName 列名
     * @return 时间对象
     * @throws SQLException SQL 异常
     */
    private static LocalDateTime toLocalDateTime(ResultSet rs, String columnName) throws SQLException {
        Timestamp timestamp = rs.getTimestamp(columnName);
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }

    /**
     * 从结果集读取可空长整型列
     *
     * @param rs         结果集
     * @param columnName 列名
     * @return 长整型值
     * @throws SQLException SQL 异常
     */
    private static Long getLong(ResultSet rs, String columnName) throws SQLException {
        long value = rs.getLong(columnName);
        return rs.wasNull() ? null : value;
    }

    /**
     * 截断超长文本
     *
     * @param text      原始文本
     * @param maxLength 最大长度
     * @return 截断后的文本
     */
    private String truncate(String text, Integer maxLength) {
        if (text == null || maxLength == null || maxLength <= 0 || text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength);
    }
}
