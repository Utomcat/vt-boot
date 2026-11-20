package com.ranyk.vt.boot.example.system.dict.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.example.system.dict.converter.SysDictTypeConverter;
import com.ranyk.vt.boot.example.system.dict.domain.dto.DictTypeFormDTO;
import com.ranyk.vt.boot.example.system.dict.domain.dto.DictTypeQueryDTO;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictData;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictType;
import com.ranyk.vt.boot.example.system.dict.domain.vo.DictTypeVO;
import com.ranyk.vt.boot.example.system.dict.mapper.SysDictDataMapper;
import com.ranyk.vt.boot.example.system.dict.mapper.SysDictTypeMapper;
import com.ranyk.vt.boot.example.system.dict.reader.DictReader;
import com.ranyk.vt.boot.web.vo.MultiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CLASS_NAME: SysDictTypeService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型管理服务, 提供字典类型的分页查询、详情、增删改与启停能力
 * <p>
 * 管理侧查询直接访问数据库; 在类型编码级联变更、删除与启停后, 通过 {@link DictReader} 按字典类型精确失效读取缓存。
 * </p>
 * @date: 2026-11-20
 */
@Service
@RequiredArgsConstructor
public class SysDictTypeService {

    /**
     * 状态: 正常
     */
    private static final int STATUS_NORMAL = 1;

    /**
     * 字典类型数据访问对象
     */
    private final SysDictTypeMapper sysDictTypeMapper;
    /**
     * 字典数据数据访问对象, 用于删除校验与类型编码变更时的级联更新
     */
    private final SysDictDataMapper sysDictDataMapper;
    /**
     * 字典类型转换器
     */
    private final SysDictTypeConverter sysDictTypeConverter;
    /**
     * 字典读取能力(带缓存), 类型变更后按字典类型精确失效
     */
    private final DictReader dictReader;

    /**
     * 按字典名称、类型编码和状态分页查询字典类型。查询条件为空时自动忽略, 结果按创建时间倒序排列。
     *
     * @param queryDTO 分页查询参数
     * @return 分页结果
     */
    public MultiResult<DictTypeVO> page(DictTypeQueryDTO queryDTO) {
        Page<SysDictType> page = new Page<>(queryDTO.getPage(), queryDTO.getSize());
        LambdaQueryWrapper<SysDictType> wrapper = Wrappers.<SysDictType>lambdaQuery()
            .like(StrUtil.isNotBlank(queryDTO.getDictName()), SysDictType::getDictName, queryDTO.getDictName())
            .like(StrUtil.isNotBlank(queryDTO.getDictType()), SysDictType::getDictType, queryDTO.getDictType())
            .eq(ObjectUtil.isNotNull(queryDTO.getStatus()), SysDictType::getStatus, queryDTO.getStatus())
            .orderByDesc(SysDictType::getCreateTime)
            .orderByDesc(SysDictType::getId);
        IPage<SysDictType> result = sysDictTypeMapper.selectPage(page, wrapper);
        List<DictTypeVO> list = result.getRecords().stream().map(sysDictTypeConverter::toVO).toList();
        return MultiResult.successMulti(list, result.getTotal(), (int) result.getCurrent(), (int) result.getSize());
    }

    /**
     * 查询全部启用字典类型的精简列表, 供字典数据页面选择所属类型使用, 按名称与主键升序
     *
     * @return 字典类型列表
     */
    public List<DictTypeVO> options() {
        return sysDictTypeMapper.selectList(Wrappers.<SysDictType>lambdaQuery()
                .eq(SysDictType::getStatus, STATUS_NORMAL)
                .orderByAsc(SysDictType::getDictName)
                .orderByAsc(SysDictType::getId))
            .stream()
            .map(sysDictTypeConverter::toVO)
            .toList();
    }

    /**
     * 根据ID查询字典类型详情, 不存在时抛出业务异常
     *
     * @param id 字典类型ID
     * @return 字典类型详情
     */
    public DictTypeVO detail(Long id) {
        return sysDictTypeConverter.toVO(getExist(id));
    }

    /**
     * 新增字典类型。会校验名称与类型编码必填、类型编码全局唯一; 未指定的状态默认正常。
     *
     * @param formDTO 字典类型表单参数
     * @return 新增字典类型ID
     */
    @Transactional(rollbackFor = Exception.class)
    public Long create(DictTypeFormDTO formDTO) {
        if (StrUtil.isBlank(formDTO.getDictName())) {
            throw new ServiceException("字典名称不能为空");
        }
        if (StrUtil.isBlank(formDTO.getDictType())) {
            throw new ServiceException("字典类型编码不能为空");
        }
        if (existsDictType(formDTO.getDictType(), null)) {
            throw new ServiceException("字典类型编码已存在");
        }
        SysDictType dictType = sysDictTypeConverter.toEntity(formDTO);
        dictType.setStatus(ObjectUtil.isNull(formDTO.getStatus()) ? STATUS_NORMAL : formDTO.getStatus());
        sysDictTypeMapper.insert(dictType);
        // 新建类型后失效可能残留的该编码空缓存, 保证后续读取能回源
        dictReader.evict(dictType.getDictType());
        return dictType.getId();
    }

    /**
     * 修改字典类型。仅更新非空字段; 当类型编码发生变更时会校验新编码唯一,
     * 并把该编码级联更新到其下所有字典数据, 保证业务引用仍然有效。
     *
     * @param formDTO 字典类型表单参数, 必须携带ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(DictTypeFormDTO formDTO) {
        if (ObjectUtil.isNull(formDTO.getId())) {
            throw new ServiceException("字典类型ID不能为空");
        }
        SysDictType dictType = getExist(formDTO.getId());
        String oldCode = dictType.getDictType();
        String newCode = formDTO.getDictType();
        if (StrUtil.isNotBlank(newCode) && !StrUtil.equals(oldCode, newCode)) {
            if (existsDictType(newCode, dictType.getId())) {
                throw new ServiceException("字典类型编码已存在");
            }
        }
        sysDictTypeConverter.updateEntity(formDTO, dictType);
        sysDictTypeMapper.updateById(dictType);
        // 类型编码变更时级联更新字典数据的所属类型, 避免数据成为孤儿
        if (StrUtil.isNotBlank(newCode) && !StrUtil.equals(oldCode, newCode)) {
            sysDictDataMapper.update(null, Wrappers.<SysDictData>lambdaUpdate()
                .set(SysDictData::getDictType, newCode)
                .eq(SysDictData::getDictType, oldCode));
        }
        // 失效受影响类型的读取缓存: 旧类型必有缓存需清理; 编码变更时新类型也需失效
        dictReader.evict(oldCode);
        if (StrUtil.isNotBlank(newCode) && !StrUtil.equals(oldCode, newCode)) {
            dictReader.evict(newCode);
        }
    }

    /**
     * 逻辑删除字典类型。存在字典数据时不允许删除, 需先清理其下的字典数据。
     *
     * @param id 字典类型ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        SysDictType dictType = getExist(id);
        Long dataCount = sysDictDataMapper.selectCount(
            Wrappers.<SysDictData>lambdaQuery().eq(SysDictData::getDictType, dictType.getDictType()));
        if (dataCount > 0) {
            throw new ServiceException("该字典类型下存在字典数据, 请先删除字典数据");
        }
        sysDictTypeMapper.deleteById(id);
        dictReader.evict(dictType.getDictType());
    }

    /**
     * 修改字典类型状态(启用/禁用)。修改前会确认字典类型存在。
     *
     * @param id     字典类型ID
     * @param status 目标状态: 1-正常 0-禁用
     */
    @Transactional(rollbackFor = Exception.class)
    public void changeStatus(Long id, Integer status) {
        if (ObjectUtil.isNull(status)) {
            throw new ServiceException("状态不能为空");
        }
        SysDictType dictType = getExist(id);
        dictType.setStatus(status);
        sysDictTypeMapper.updateById(dictType);
        dictReader.evict(dictType.getDictType());
    }

    /**
     * 判断字典类型编码是否已被占用
     *
     * @param dictType  字典类型编码
     * @param excludeId 需要排除的字典类型ID(修改场景传自身ID), 新增场景传 null
     * @return 是否已存在
     */
    private boolean existsDictType(String dictType, Long excludeId) {
        Long count = sysDictTypeMapper.selectCount(Wrappers.<SysDictType>lambdaQuery()
            .eq(SysDictType::getDictType, dictType)
            .ne(ObjectUtil.isNotNull(excludeId), SysDictType::getId, excludeId));
        return count > 0;
    }

    /**
     * 获取存在的字典类型
     *
     * @param id 字典类型ID
     * @return 字典类型实体 {@link SysDictType}
     */
    private SysDictType getExist(Long id) {
        SysDictType dictType = sysDictTypeMapper.selectById(id);
        if (ObjectUtil.isNull(dictType)) {
            throw new ServiceException("字典类型不存在");
        }
        return dictType;
    }
}
