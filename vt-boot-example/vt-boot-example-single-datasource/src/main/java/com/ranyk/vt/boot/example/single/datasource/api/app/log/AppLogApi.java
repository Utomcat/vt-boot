package com.ranyk.vt.boot.example.single.datasource.api.app.log;

import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.single.datasource.domain.dto.app.log.AppLogDTO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.DeleteAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.QueryAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.SaveAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.po.app.log.UpdateAppLogPO;
import com.ranyk.vt.boot.example.single.datasource.domain.vo.app.log.QueryAppLogVO;
import com.ranyk.vt.boot.example.single.datasource.mapper.app.log.AppLogMapper;
import com.ranyk.vt.boot.example.single.datasource.service.app.log.AppLogService;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CLASS_NAME: AppLogApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 业务日志 API 接口类
 * @date: 2026-02-26
 */
@RestController
@RequestMapping("/api/log")
public class AppLogApi {

    /**
     * 业务日志业务逻辑处理对象
     */
    private final AppLogService appLogService;
    /**
     * 业务日志对象转换 Mapper 对象
     */
    private final AppLogMapper appLogMapper;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入业务逻辑处理对象
     *
     * @param appLogService 业务日志业务逻辑处理对象
     * @param appLogMapper  业务日志对象转换 Mapper 对象
     */
    @Autowired
    public AppLogApi(AppLogService appLogService, AppLogMapper appLogMapper) {
        this.appLogService = appLogService;
        this.appLogMapper = appLogMapper;
    }

    /**
     * 保存一条业务日志数据
     *
     * @param appLogPO 业务日志数据对象 {@link SaveAppLogPO}
     * @return 保存结果, true: 保存成功; false: 保存失败;
     */
    @PostMapping
    public Result<Boolean> saveOne(@RequestBody SaveAppLogPO appLogPO) {
        return Result.success(appLogService.saveOne(appLogMapper.savePOToDTO(appLogPO)));
    }

    /**
     * 删除一条业务日志数据
     *
     * @param deleteAppLogPO 业务日志数据对象 {@link DeleteAppLogPO}
     * @return 删除结果, true: 删除成功; false: 删除失败;
     */
    @DeleteMapping
    public Result<Boolean> deleteOne(@RequestBody DeleteAppLogPO deleteAppLogPO) {
        return Result.success(appLogService.deleteOne(appLogMapper.deletePOToDTO(deleteAppLogPO)));
    }

    /**
     * 更新一条业务日志数据
     *
     * @param updateAppLogPO 业务日志数据对象 {@link UpdateAppLogPO}
     * @return 更新结果, true: 更新成功; false: 更新失败;
     */
    @PutMapping
    public Result<Boolean> updateOne(@RequestBody UpdateAppLogPO updateAppLogPO) {
        return Result.success(appLogService.updateOne(appLogMapper.updatePOToDTO(updateAppLogPO)));
    }

    /**
     * 获取业务日志列表
     *
     * @param queryAppLogPO 业务日志查询条件对象 {@link QueryAppLogPO}
     * @return 业务日志列表, {@link QueryAppLogVO} 对象 List 集合
     */
    @GetMapping("/list")
    public MultiResult<QueryAppLogVO> list(QueryAppLogPO queryAppLogPO) {
        PageResponse<AppLogDTO> appLogDTOPageResponse = appLogService.queryList(appLogMapper.queryPOToDTO(queryAppLogPO));
        return MultiResult.successMulti(appLogMapper.dtoListToQueryVOList(appLogDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(appLogDTOPageResponse.getTotal())),
                appLogDTOPageResponse.getCurrentPage(),
                appLogDTOPageResponse.getPageSize());
    }
}
