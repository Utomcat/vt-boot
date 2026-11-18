package com.ranyk.vt.boot.task.repository;

import com.ranyk.vt.boot.task.domain.po.VtJobLogPO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * CLASS_NAME: VtJobLogRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志仓储接口 - 便于使用方替换为 MyBatis-Plus/JPA 等实现
 * @date: 2026-11-18
 */
public interface VtJobLogRepository {

    /**
     * 保存一条"运行中"日志
     *
     * @param log 日志对象 {@link VtJobLogPO}
     * @return 日志主键, 保存失败返回 null
     */
    Long saveStart(VtJobLogPO log);

    /**
     * 标记日志为执行结束
     *
     * @param id         日志主键
     * @param status     结束状态, 取值见 JobLogStatusEnum
     * @param durationMs 耗时(毫秒)
     * @param errorMsg   异常信息, 无异常传 null
     */
    void markFinished(Long id, Integer status, Long durationMs, String errorMsg);

    /**
     * 按主键查询日志
     *
     * @param id 日志主键
     * @return 日志对象
     */
    Optional<VtJobLogPO> findById(Long id);

    /**
     * 删除指定时间之前的日志
     *
     * @param before 时间点
     * @return 删除条数
     */
    int deleteBefore(LocalDateTime before);

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
    List<VtJobLogPO> query(String jobName, Integer status, LocalDateTime from, LocalDateTime to, int offset, int limit);

    /**
     * 统计日志条数
     *
     * @param jobName 任务名称, 可为空
     * @param status  状态, 可为空
     * @param from    开始时间, 可为空
     * @param to      结束时间, 可为空
     * @return 总条数
     */
    long count(String jobName, Integer status, LocalDateTime from, LocalDateTime to);
}
