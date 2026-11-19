package com.ranyk.vt.boot.example.system.dict.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.system.dict.converter.SysDictDataConverter;
import com.ranyk.vt.boot.example.system.dict.domain.dto.DictDataFormDTO;
import com.ranyk.vt.boot.example.system.dict.domain.dto.DictDataQueryDTO;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictData;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictType;
import com.ranyk.vt.boot.example.system.dict.domain.vo.DictDataVO;
import com.ranyk.vt.boot.example.system.dict.mapper.SysDictDataMapper;
import com.ranyk.vt.boot.example.system.dict.mapper.SysDictTypeMapper;
import com.ranyk.vt.boot.example.system.dict.reader.DictReader;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CLASS_NAME: SysDictDataService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据管理服务, 提供字典数据的分页查询、按类型查询、详情、增删改与启停能力
 * <p>
 * 读取入口 {@link #listByType(String)} 通过 {@link DictReader} 走缓存(未命中回源并回填);
 * 管理侧的分页查询直接访问数据库, 并在新增/修改/删除/启停后按字典类型精确失效缓存, 保证读写一致性。
 * </p>
 * @date: 2026-11-20
 */
@Service
@RequiredArgsConstructor
public class SysDictDataService {

    /**
     * 状态: 正常
     */
    private static final int STATUS_NORMAL = 1;
    /**
     * 是否默认选中: 否
     */
    private static final int DEFAULT_NO = 0;

    /**
     * 字典数据数据访问对象
     */
    private final SysDictDataMapper sysDictDataMapper;
    /**
     * 字典类型数据访问对象, 用于校验所属类型是否存在
     */
    private final SysDictTypeMapper sysDictTypeMapper;
    /**
     * 字典数据转换器
     */
    private final SysDictDataConverter sysDictDataConverter;
    /**
     * 字典读取能力(带缓存), 读取走缓存, 写入后按字典类型精确失效
     */
    private final DictReader dictReader;

    /**
     * 按所属类型、标签和状态分页查询字典数据。结果按排序值与主键升序排列。
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    public MultiResult<DictDataVO> page(DictDataQueryDTO queryDTO) {
        Page<SysDictData> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<SysDictData> wrapper = Wrappers.<SysDictData>lambdaQuery()
                .eq(StrUtil.isNotBlank(queryDTO.getDictType()), SysDictData::getDictType, queryDTO.getDictType())
                .like(StrUtil.isNotBlank(queryDTO.getDictLabel()), SysDictData::getDictLabel, queryDTO.getDictLabel())
                .eq(ObjectUtil.isNotNull(queryDTO.getStatus()), SysDictData::getStatus, queryDTO.getStatus())
                .orderByAsc(SysDictData::getDictSort)
                .orderByAsc(SysDictData::getId);
        IPage<SysDictData> result = sysDictDataMapper.selectPage(page, wrapper);
        List<DictDataVO> list = result.getRecords().stream().map(sysDictDataConverter::toVO).toList();
        return MultiResult.successMulti(list, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * 按字典类型编码查询全部启用状态字典数据, 按排序值与主键升序。该方法是业务读取字典的标准入口,
     * 通过 {@link DictReader} 读取并命中缓存。
     *
     * @param dictType 字典类型编码
     * @return 字典数据列表
     */
    public List<DictDataVO> listByType(String dictType) {
        if (StrUtil.isBlank(dictType)) {
            throw new ServiceException("字典类型编码不能为空");
        }
        return dictReader.listByType(dictType).stream()
                .map(item -> sysDictDataConverter.toVO(item))
                .toList();
    }

    /**
     * 根据ID查询字典数据详情, 不存在时抛出业务异常
     *
     * @param id 字典数据ID
     * @return 字典数据详情
     */
    public DictDataVO detail(Long id) {
        return sysDictDataConverter.toVO(getExist(id));
    }

    /**
     * 新增字典数据。会校验所属类型存在、标签与键值必填、键值在同一类型下唯一;
     * 未指定的排序值为 0、非默认、状态正常。
     *
     * @param formDTO 字典数据表单参数
     * @return 新增字典数据ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(DictDataFormDTO formDTO) {
        validateDictTypeExists(formDTO.getDictType());
        if (StrUtil.isBlank(formDTO.getDictLabel())) {
            throw new ServiceException("字典标签不能为空");
        }
        if (StrUtil.isBlank(formDTO.getDictValue())) {
            throw new ServiceException("字典键值不能为空");
        }
        if (existsDictValue(formDTO.getDictType(), formDTO.getDictValue(), null)) {
            throw new ServiceException("同一字典类型下字典键值不能重复");
        }
        SysDictData dictData = sysDictDataConverter.toEntity(formDTO);
        dictData.setDictSort(ObjectUtil.isNull(formDTO.getDictSort()) ? 0 : formDTO.getDictSort());
        dictData.setIsDefault(ObjectUtil.isNull(formDTO.getIsDefault()) ? DEFAULT_NO : formDTO.getIsDefault());
        dictData.setStatus(ObjectUtil.isNull(formDTO.getStatus()) ? STATUS_NORMAL : formDTO.getStatus());
        sysDictDataMapper.insert(dictData);
        dictReader.evict(dictData.getDictType());
        return dictData.getId();
    }

    /**
     * 修改字典数据。仅更新非空字段; 当键值或所属类型发生变更时会重新校验键值在目标类型下唯一。
     *
     * @param formDTO 字典数据表单参数, 必须携带ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DictDataFormDTO formDTO) {
        if (ObjectUtil.isNull(formDTO.getId())) {
            throw new ServiceException("字典数据ID不能为空");
        }
        SysDictData dictData = getExist(formDTO.getId());
        String oldType = dictData.getDictType();
        String targetType = StrUtil.isNotBlank(formDTO.getDictType()) ? formDTO.getDictType() : dictData.getDictType();
        String targetValue = StrUtil.isNotBlank(formDTO.getDictValue()) ? formDTO.getDictValue() : dictData.getDictValue();
        boolean typeChanged = !StrUtil.equals(targetType, dictData.getDictType());
        boolean valueChanged = !StrUtil.equals(targetValue, dictData.getDictValue());
        if (typeChanged) {
            validateDictTypeExists(targetType);
        }
        if (typeChanged || valueChanged) {
            if (existsDictValue(targetType, targetValue, dictData.getId())) {
                throw new ServiceException("同一字典类型下字典键值不能重复");
            }
        }
        sysDictDataConverter.updateEntity(formDTO, dictData);
        sysDictDataMapper.updateById(dictData);
        dictReader.evict(oldType);
        if (!StrUtil.equals(oldType, dictData.getDictType())) {
            dictReader.evict(dictData.getDictType());
        }
    }

    /**
     * 逻辑删除字典数据。删除前先确认数据存在。
     *
     * @param id 字典数据ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysDictData dictData = getExist(id);
        sysDictDataMapper.deleteById(id);
        dictReader.evict(dictData.getDictType());
    }

    /**
     * 修改字典数据状态(启用/禁用)。修改前会确认数据存在。
     *
     * @param id     字典数据ID
     * @param status 目标状态: 1-正常 0-禁用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        if (ObjectUtil.isNull(status)) {
            throw new ServiceException("状态不能为空");
        }
        SysDictData dictData = getExist(id);
        dictData.setStatus(status);
        sysDictDataMapper.updateById(dictData);
        dictReader.evict(dictData.getDictType());
    }

    /**
     * 校验字典类型编码对应的类型存在
     *
     * @param dictType 字典类型编码
     */
    private void validateDictTypeExists(String dictType) {
        if (StrUtil.isBlank(dictType)) {
            throw new ServiceException("所属字典类型不能为空");
        }
        Long count = sysDictTypeMapper.selectCount(
                Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictType, dictType));
        if (count <= 0) {
            throw new ServiceException("所属字典类型不存在");
        }
    }

    /**
     * 判断同一字典类型下字典键值是否已被占用
     *
     * @param dictType  字典类型编码
     * @param dictValue 字典键值
     * @param excludeId 需要排除的字典数据ID(修改场景传自身ID), 新增场景传 null
     * @return 是否已存在
     */
    private boolean existsDictValue(String dictType, String dictValue, Long excludeId) {
        Long count = sysDictDataMapper.selectCount(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictValue, dictValue)
                .ne(ObjectUtil.isNotNull(excludeId), SysDictData::getId, excludeId));
        return count > 0;
    }

    /**
     * 获取存在的字典数据
     *
     * @param id 字典数据ID
     * @return 字典数据实体 {@link SysDictData}
     */
    private SysDictData getExist(Long id) {
        SysDictData dictData = sysDictDataMapper.selectById(id);
        if (ObjectUtil.isNull(dictData)) {
            throw new ServiceException("字典数据不存在");
        }
        return dictData;
    }
}
