package com.ranyk.vt.boot.example.web.freamwork.api.department;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.dto.DepartmentDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.DeleteDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.QueryDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.SaveDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.po.UpdateDepartmentPO;
import com.ranyk.vt.boot.example.web.freamwork.domain.department.vo.QueryDepartmentVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.department.DepartmentMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.department.DepartmentService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CLASS_NAME: DepartmentApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 部门信息请求接口API
 * @date: 2026-03-05
 */
@RestController
@RequestMapping("/api/department")
public class DepartmentApi {

    /**
     * 部门信息数据转换接口对象
     */
    private final DepartmentMapper departmentMapper;
    /**
     * 部门信息服务业务逻辑对象
     */
    private final DepartmentService departmentService;

    /**
     * 部门信息请求接口API构造函数 - 向 Spring IOC 容器中注入部门信息数据转换接口对象和部门信息服务业务逻辑对象
     *
     * @param departmentMapper  部门信息数据转换接口对象
     * @param departmentService 部门信息服务业务逻辑对象
     */
    @Autowired
    public DepartmentApi(DepartmentMapper departmentMapper, DepartmentService departmentService) {
        this.departmentMapper = departmentMapper;
        this.departmentService = departmentService;
    }

    /**
     * 保存一个部门信息
     *
     * @param saveDepartmentPO 部门信息数据传输对象 {@link SaveDepartmentPO}
     * @return 保存结果 {@link Result} true: 保存成功; false: 保存失败;
     */
    @PostMapping
    @SaCheckPermission(value = {"add:department"})
    @Log(operation = "保存一个部门信息", type = Log.LogType.SELECT)
    public Result<Boolean> saveDepartment(@RequestBody SaveDepartmentPO saveDepartmentPO) {
        departmentService.saveOneDepartment(departmentMapper.saveDepartmentPOToDTO(saveDepartmentPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一个部门信息
     *
     * @param deleteDepartmentPO 部门信息数据传输对象 {@link DeleteDepartmentPO}
     * @return 删除结果 {@link Result} true: 删除成功; false: 删除失败;
     */
    @DeleteMapping
    @SaCheckPermission(value = {"delete:department"})
    @Log(operation = "删除一个部门信息", type = Log.LogType.DELETE)
    public Result<Boolean> deleteDepartment(@RequestBody DeleteDepartmentPO deleteDepartmentPO) {
        departmentService.deleteOneDepartment(departmentMapper.deleteDepartmentPOToDTO(deleteDepartmentPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 修改一个部门信息
     *
     * @param updateDepartmentPO 部门信息数据传输对象 {@link UpdateDepartmentPO}
     * @return 修改结果 {@link Result} true: 修改成功; false: 修改失败;
     */
    @PutMapping
    @SaCheckPermission(value = {"update:department"})
    @Log(operation = "修改一个部门信息", type = Log.LogType.UPDATE)
    public Result<Boolean> updateDepartment(@RequestBody UpdateDepartmentPO updateDepartmentPO) {
        departmentService.updateOneDepartment(departmentMapper.updateDepartmentPOToDTO(updateDepartmentPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询部门信息 - 分页
     *
     * @param queryDepartmentPO 部门信息数据传输对象 {@link QueryDepartmentPO}
     * @return 查询结果 {@link MultiResult}
     */
    @GetMapping
    @SaCheckPermission(value = {"query:department"})
    @Log(operation = "查询部门信息 - 分页", type = Log.LogType.SELECT)
    public MultiResult<QueryDepartmentVO> queryDepartment(QueryDepartmentPO queryDepartmentPO) {
        PageResponse<DepartmentDTO> departmentDTOPageResponse = departmentService.queryDepartment(departmentMapper.queryDepartmentPOToDTO(queryDepartmentPO));
        return MultiResult.successMulti(departmentMapper.dtoListToQueryDepartmentVOList(departmentDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(departmentDTOPageResponse.getTotal())),
                departmentDTOPageResponse.getCurrentPage(),
                departmentDTOPageResponse.getPageSize());
    }
}
