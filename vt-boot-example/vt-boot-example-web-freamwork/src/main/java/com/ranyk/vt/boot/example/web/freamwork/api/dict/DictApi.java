package com.ranyk.vt.boot.example.web.freamwork.api.dict;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto.DictDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto.DictTypeDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.po.*;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo.DictTypeVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo.DictVO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo.QueryDictVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.dict.DictMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.dict.DictService;
import com.ranyk.vt.boot.example.web.freamwork.service.dict.DictTypeService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.MultiResult;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CLASS_NAME: DictApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典接口API
 * @date: 2026-03-05
 */
@RestController
@RequestMapping("/api/dict")
public class DictApi {

    /**
     * 字典信息业务逻辑类对象
     */
    private final DictService dictService;
    /**
     * 字典类型业务逻辑类对象
     */
    private final DictTypeService dictTypeService;
    /**
     * 字典信息数据转换接口对象
     */
    private final DictMapper dictMapper;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入 字典信息业务逻辑类对象、字典类型业务逻辑类对象
     *
     * @param dictService     字典信息业务逻辑类对象
     * @param dictTypeService 字典类型业务逻辑类对象
     * @param dictMapper      字典信息数据转换接口对象
     */
    @Autowired
    public DictApi(DictService dictService, DictTypeService dictTypeService, DictMapper dictMapper) {
        this.dictService = dictService;
        this.dictTypeService = dictTypeService;
        this.dictMapper = dictMapper;
    }

    /**
     *
     * 新增一个字典类型操作
     *
     * @param saveDictTypePO 保存字典类型 PO 对象 {@link SaveDictTypePO}
     * @return 响应结果对象 {@link Result} - true: 新增成功; false: 新增失败;
     */
    @PostMapping("/type")
    @SaCheckPermission(value = {"add:dict:type"})
    @Log(operation = "新增一个字典类型操作", type = Log.LogType.INSERT)
    public Result<Boolean> saveDictType(@RequestBody SaveDictTypePO saveDictTypePO) {
        dictTypeService.saveOneDictType(dictMapper.saveDictTypePOToDTO(saveDictTypePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 新增一个字典信息操作
     *
     * @param dictPO 保存字典信息 PO 对象 {@link SaveDictPO}
     * @return 响应结果对象 {@link Result} - true: 新增成功; false: 新增失败;
     */
    @PostMapping
    @SaCheckPermission(value = {"add:dict"})
    @Log(operation = "新增一个字典信息操作", type = Log.LogType.INSERT)
    public Result<Boolean> saveDict(@RequestBody SaveDictPO dictPO) {
        dictService.saveOneDict(dictMapper.saveDictPOToDTO(dictPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一个字典类型操作
     *
     * @param deleteDictTypePO 删除字典类型 PO 删除字典类型 PO 对象 {@link DeleteDictTypePO}
     * @return 响应结果对象 {@link Result} - true: 删除成功; false: 删除失败;
     */
    @DeleteMapping("/type")
    @SaCheckPermission(value = {"delete:dict:type"})
    @Log(operation = "删除一个字典类型操作", type = Log.LogType.DELETE)
    public Result<Boolean> deleteDictType(@RequestBody DeleteDictTypePO deleteDictTypePO) {
        dictTypeService.deleteOnDictType(dictMapper.deleteDictTypePOToDTO(deleteDictTypePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 删除一个字典信息操作
     *
     * @param deleteDictPO 删除字典信息 PO 对象 {@link DeleteDictPO}
     * @return 响应结果对象 {@link Result} - true: 删除成功; false: 删除失败;
     */
    @DeleteMapping
    @SaCheckPermission(value = {"delete:dict"})
    @Log(operation = "删除一个字典信息操作", type = Log.LogType.DELETE)
    public Result<Boolean> deleteDict(@RequestBody DeleteDictPO deleteDictPO) {
        dictService.deleteOneDict(dictMapper.deleteDictPOToDTO(deleteDictPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 修改一个字典类型操作
     *
     * @param updateDictTypePO 修改字典类型 PO 对象 {@link UpdateDictTypePO}
     * @return 响应结果对象 {@link Result} - true: 修改成功; false: 修改失败;
     */
    @PutMapping("/type")
    @SaCheckPermission(value = {"update:dict:type"})
    @Log(operation = "修改一个字典类型操作", type = Log.LogType.UPDATE)
    public Result<Boolean> updateDictType(@RequestBody UpdateDictTypePO updateDictTypePO) {
        dictTypeService.updateOneDictType(dictMapper.updateDictTypePOToDTO(updateDictTypePO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 修改一个字典信息操作
     *
     * @param updateDictPO 修改字典信息 PO 对象 {@link UpdateDictPO}
     * @return 响应结果对象 {@link Result} - true: 修改成功; false: 修改失败;
     */
    @PutMapping
    @SaCheckPermission(value = {"update:dict"})
    @Log(operation = "修改一个字典信息操作", type = Log.LogType.UPDATE)
    public Result<Boolean> updateDict(@RequestBody UpdateDictPO updateDictPO) {
        dictService.updateOneDict(dictMapper.updateDictPOToDTO(updateDictPO));
        return Result.success(Boolean.TRUE);
    }

    /**
     * 查询字典类型操作  - 分页
     *
     * @param queryDictTypePO 查询字典类型信息 PO 对象 {@link QueryDictTypePO}
     * @return 响应结果对象 {@link MultiResult} - 字典信息列表 {@link DictTypeDTO}
     */
    @GetMapping("/type")
    @SaCheckPermission(value = {"query:dict:type"})
    @Log(operation = "查询字典类型操作  - 分页", type = Log.LogType.SELECT)
    public MultiResult<DictTypeVO> getDictType(QueryDictTypePO queryDictTypePO) {
        PageResponse<DictTypeDTO> dictTypeDTOPageResponse = dictTypeService.queryDictTypeList(dictMapper.queryDictTypePOToDTO(queryDictTypePO));
        return MultiResult.successMulti(dictMapper.dictTypeDTOListToVOList(dictTypeDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(dictTypeDTOPageResponse.getTotal())),
                dictTypeDTOPageResponse.getCurrentPage(),
                dictTypeDTOPageResponse.getPageSize());
    }

    /**
     * 获取字典信息操作 - 分页
     *
     * @param queryDictPO 获取字典信息 PO 获取字典信息 PO 对象 {@link QueryDictPO}
     * @return 响应结果对象 {@link MultiResult} - 字典信息列表 {@link DictVO}
     */
    @GetMapping
    @SaCheckPermission(value = {"query:dict"})
    @Log(operation = "获取字典信息操作 - 分页", type = Log.LogType.SELECT)
    public MultiResult<DictVO> getDict(QueryDictPO queryDictPO) {
        PageResponse<DictDTO> dictDTOPageResponse = dictService.queryDictList(dictMapper.queryDictPOToDTO(queryDictPO));
        return MultiResult.successMulti(dictMapper.dictDTOListToVOList(dictDTOPageResponse.getData()),
                Long.parseLong(String.valueOf(dictDTOPageResponse.getTotal())),
                dictDTOPageResponse.getCurrentPage(),
                dictDTOPageResponse.getPageSize()
        );
    }

    /**
     * 根据字典类型 code 查询字典数据操作
     *
     * @param queryDictByTypeCodePO 查询字典数据 PO 对象 {@link QueryDictByTypeCodePO}
     * @return 响应结果对象 {@link Result} - 字典数据列表 - {@link QueryDictVO}
     */
    @GetMapping("/by/type/code")
    @SaCheckPermission(value = {"query:dict"})
    @Log(operation = "根据字典类型 code 查询字典数据操作", type = Log.LogType.SELECT)
    public Result<List<QueryDictVO>> getDictByTypeCode(QueryDictByTypeCodePO queryDictByTypeCodePO) {
        List<DictDTO> dictDTOList = dictService.queryDictByTypeCode(dictMapper.queryDictByTypeCodePOToDictDTO(queryDictByTypeCodePO));
        return Result.success(dictMapper.dictDTOToQueryDictVO(dictDTOList));
    }

}
