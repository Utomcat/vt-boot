package com.ranyk.vt.boot.task.admin.service;

import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.task.admin.domain.dto.VtJobLogQueryDTO;
import com.ranyk.vt.boot.task.admin.domain.vo.VtJobLogVO;
import com.ranyk.vt.boot.task.constant.JobLogStatusEnum;
import com.ranyk.vt.boot.task.domain.po.VtJobLogPO;
import com.ranyk.vt.boot.task.repository.VtJobLogRepository;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CLASS_NAME: VtJobLogService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 任务执行日志业务类 - 未启用执行日志(无数据源)时给出明确提示
 * @date: 2026-11-18
 */
@Slf4j
@RequiredArgsConstructor
public class VtJobLogService {

    /**
     * 任务执行日志仓储提供者
     */
    private final ObjectProvider<VtJobLogRepository> repositoryProvider;

    /**
     * 分页查询执行日志
     *
     * @param queryDTO 分页查询参数
     * @return 包含当前页日志列表与总数的分页结果
     */
    public MultiResult<VtJobLogVO> page(VtJobLogQueryDTO queryDTO) {
        VtJobLogRepository repository = requireRepository();
        int page = normalizePage(queryDTO.getPage());
        int size = normalizeSize(queryDTO.getSize());
        long total = repository.count(queryDTO.getJobName(), queryDTO.getStatus(), queryDTO.getFrom(), queryDTO.getTo());
        List<VtJobLogVO> list = repository
                .query(queryDTO.getJobName(), queryDTO.getStatus(), queryDTO.getFrom(), queryDTO.getTo(), (page - 1) * size, size)
                .stream()
                .map(this::toVO)
                .toList();
        return MultiResult.successMulti(list, total, page, size);
    }

    /**
     * 查询执行日志详情
     *
     * @param id 日志主键
     * @return 日志详情
     */
    public VtJobLogVO detail(Long id) {
        return requireRepository().findById(id)
                .map(this::toVO)
                .orElseThrow(() -> new ServiceException("执行日志不存在: " + id));
    }

    /**
     * 按保留天数清理历史日志
     *
     * @param days 保留天数, 小于 1 时按 30 天处理
     * @return 清理条数
     */
    public int clean(Integer days) {
        int keepDays = days == null || days < 1 ? 30 : days;
        int deleted = requireRepository().deleteBefore(LocalDateTime.now().minusDays(keepDays));
        log.debug("VtJobLog cleaned. keepDays: {}, deleted: {}", keepDays, deleted);
        return deleted;
    }

    /**
     * 统计各状态日志数量
     *
     * @return 状态 -> 数量
     */
    public Map<String, Long> summary() {
        VtJobLogRepository repository = requireRepository();
        Map<String, Long> result = new LinkedHashMap<>();
        for (JobLogStatusEnum status : JobLogStatusEnum.values()) {
            result.put(status.name().toLowerCase(), repository.count(null, status.getValue(), null, null));
        }
        return result;
    }

    /**
     * 获取日志仓储, 未启用时抛出业务异常
     *
     * @return 日志仓储 {@link VtJobLogRepository}
     */
    private VtJobLogRepository requireRepository() {
        VtJobLogRepository repository = repositoryProvider.getIfAvailable();
        if (repository == null) {
            throw new ServiceException("未启用任务执行日志, 请引入数据源并开启 vt.task.log.enabled");
        }
        return repository;
    }

    /**
     * 把持久化对象转换为响应对象
     *
     * @param po 持久化对象 {@link VtJobLogPO}
     * @return 响应对象 {@link VtJobLogVO}
     */
    private VtJobLogVO toVO(VtJobLogPO po) {
        return VtJobLogVO.builder()
                .id(po.getId())
                .jobName(po.getJobName())
                .jobGroup(po.getJobGroup())
                .triggerName(po.getTriggerName())
                .fireTime(po.getFireTime())
                .startTime(po.getStartTime())
                .endTime(po.getEndTime())
                .durationMs(po.getDurationMs())
                .status(po.getStatus())
                .params(po.getParams())
                .errorMsg(po.getErrorMsg())
                .instanceId(po.getInstanceId())
                .tenantId(po.getTenantId())
                .createTime(po.getCreateTime())
                .build();
    }

    /**
     * 归一化页码
     *
     * @param page 页码
     * @return 归一化后的页码
     */
    private int normalizePage(Integer page) {
        return page == null || page < 1 ? 1 : page;
    }

    /**
     * 归一化每页条数
     *
     * @param size 每页条数
     * @return 归一化后的每页条数
     */
    private int normalizeSize(Integer size) {
        if (size == null || size < 1) {
            return 10;
        }
        return Math.min(size, 200);
    }
}
