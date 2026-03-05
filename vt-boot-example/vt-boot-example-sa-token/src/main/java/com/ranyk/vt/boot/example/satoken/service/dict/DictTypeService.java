package com.ranyk.vt.boot.example.satoken.service.dict;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateType;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.satoken.domain.dict.dto.DictTypeDTO;
import com.ranyk.vt.boot.example.satoken.domain.dict.entity.DictType;
import com.ranyk.vt.boot.example.satoken.mapper.dict.DictMapper;
import com.ranyk.vt.boot.example.satoken.repository.dict.DictTypeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * CLASS_NAME: DictTypeService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型业务逻辑类
 * @date: 2026-03-05
 */
@Slf4j
@Service
public class DictTypeService extends ServiceImpl<DictTypeRepository, DictType> {

    /**
     * 字典类型数据操作接口对象
     */
    private final DictTypeRepository dictTypeRepository;
    /**
     * 字典数据操作接口对象
     */
    private final DictMapper dictMapper;

    /**
     * 构造方法 - 向 Spring IOC 容器中注入字典类型数据操作接口对象
     *
     * @param dictTypeRepository 字典类型数据操作接口对象, {@link DictTypeRepository}
     * @param dictMapper         字典数据操作接口对象, {@link DictMapper}
     */
    @Autowired
    public DictTypeService(DictTypeRepository dictTypeRepository, DictMapper dictMapper) {
        this.dictTypeRepository = dictTypeRepository;
        this.dictMapper = dictMapper;
    }

    /**
     * 新增一条字典类型数据
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneDictType(DictTypeDTO dictTypeDTO) {
        // 校验字典类型名、字典类型编码是否存在值
        verifyDictTypeParams(dictTypeDTO, OperateType.SAVE);
        // 判定当前数据库中是否存在字典类型 code
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictType::getCode, dictTypeDTO.getCode());
        Long count = dictTypeRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("新增字典类型时, 字典类型编码 {} 已存在!", dictTypeDTO.getCode());
            throw new ServiceException("新增字典类型时, 字典类型编码 %s 已存在!".formatted(dictTypeDTO.getCode()));
        }
        // 数据转换
        DictType dictType = dictMapper.dictTypeDTOToEntity(dictTypeDTO);
        // 保存字典类型
        boolean saveResult = saveOrUpdate(dictType);
        // 判断结果是否成功
        if (!saveResult) {
            log.error("新增字典类型失败!");
            throw new ServiceException("新增字典类型失败!");
        }
    }

    /**
     * 删除一条字典类型数据
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOnDictType(DictTypeDTO dictTypeDTO) {
        verifyDictTypeParams(dictTypeDTO, OperateType.DELETE);
        boolean deleteResult = removeById(dictTypeDTO.getId());
        if (!deleteResult) {
            log.error("删除字典类型失败!");
            throw new ServiceException("删除字典类型失败!");
        }
    }

    /**
     * 修改一条字典类型数据
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneDictType(DictTypeDTO dictTypeDTO) {
        verifyDictTypeParams(dictTypeDTO, OperateType.UPDATE);
        // 判定当前数据库中是否存在字典类型 code, 且 code 不能是当前字典类型的 code
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DictType::getCode, dictTypeDTO.getCode());
        Long count = dictTypeRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("更新字典类型时, 字典类型编码 {} 已存在!", dictTypeDTO.getCode());
            throw new ServiceException("更新字典类型时, 字典类型编码 %s 已存在!".formatted(dictTypeDTO.getCode()));
        }
        DictType dictType = dictMapper.dictTypeDTOToEntity(dictTypeDTO);
        // 更新字典类型
        boolean updateResult = saveOrUpdate(dictType);
        if (!updateResult) {
            log.error("更新字典类型失败!");
            throw new ServiceException("更新字典类型失败!");
        }
    }

    /**
     * 查询字典类型列表
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     * @return 字典类型列表, {@link PageResponse} - {@link DictTypeDTO}
     */
    public PageResponse<DictTypeDTO> queryDictTypeList(DictTypeDTO dictTypeDTO) {
        Page<DictType> page = PageUtils.buildPage(dictTypeDTO);
        LambdaQueryWrapper<DictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StrUtil.isNotBlank(dictTypeDTO.getName()), DictType::getName, dictTypeDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(dictTypeDTO.getCode()), DictType::getCode, dictTypeDTO.getCode());
        queryWrapper.like(StrUtil.isNotBlank(dictTypeDTO.getRemark()), DictType::getRemark, dictTypeDTO.getRemark());
        IPage<DictType> list = page(page, queryWrapper);
        return PageUtils.buildPageResponse(list, dictMapper.dictTypeEntityListToDTOList(list.getRecords()));
    }

    /**
     * 验证字典类型参数
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     * @param operateType 操作类型, {@link OperateType}
     */
    private void verifyDictTypeParams(DictTypeDTO dictTypeDTO, OperateType operateType) {
        switch (operateType) {
            case SAVE -> verifySaveDictTypeParams(dictTypeDTO);
            case UPDATE -> verifyUpdateDictTypeParams(dictTypeDTO);
            case DELETE -> verifyDeleteDictTypeParams(dictTypeDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 新增字典类型数据参数验证
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     */
    private void verifySaveDictTypeParams(DictTypeDTO dictTypeDTO) {
        if (StrUtil.isBlank(dictTypeDTO.getName())) {
            log.error("新增字典类型失败，字典类型名不能为空!");
            throw new ServiceException("新增字典类型失败，字典类型名不能为空!");
        }
        if (StrUtil.isBlank(dictTypeDTO.getCode())) {
            log.error("新增字典类型失败，字典类型编码不能为空!");
            throw new ServiceException("新增字典类型失败，字典类型编码不能为空!");
        }
    }

    /**
     * 修改字典类型数据参数验证
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     */
    private void verifyUpdateDictTypeParams(DictTypeDTO dictTypeDTO) {
        if (StrUtil.isBlank(dictTypeDTO.getId())) {
            log.error("修改字典类型失败，数据 ID 不能为空!");
            throw new ServiceException("修改字典类型失败，数据 ID 不能为空!");
        }
        if (StrUtil.isBlank(dictTypeDTO.getName())) {
            log.error("修改字典类型失败，字典类型名不能为空!");
            throw new ServiceException("修改字典类型失败，字典类型名不能为空!");
        }
        if (StrUtil.isBlank(dictTypeDTO.getCode())) {
            log.error("修改字典类型失败，字典类型编码不能为空!");
            throw new ServiceException("修改字典类型失败，字典类型编码不能为空!");
        }
    }

    /**
     * 删除字典类型数据参数验证
     *
     * @param dictTypeDTO 字典类型 DTO 对象, {@link DictTypeDTO}
     */
    private void verifyDeleteDictTypeParams(DictTypeDTO dictTypeDTO) {
        if (StrUtil.isBlank(dictTypeDTO.getId()) && CollectionUtil.isEmpty(dictTypeDTO.getIds())) {
            log.error("删除字典类型失败，数据 ID 或字典类型 ID 列表不能为空!");
            throw new ServiceException("删除字典类型失败，数据 ID 或字典类型 ID 列表不能为空!");
        }
    }

}
