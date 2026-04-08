package com.ranyk.vt.boot.example.web.freamwork.service.dict;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranyk.vt.boot.base.constant.OperateTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.datasource.util.PageUtils;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.dto.DictDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.entity.Dict;
import com.ranyk.vt.boot.example.web.freamwork.domain.dict.entity.DictType;
import com.ranyk.vt.boot.example.web.freamwork.mapper.dict.DictMapper;
import com.ranyk.vt.boot.example.web.freamwork.repository.dict.DictRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * CLASS_NAME: DictService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典业务逻辑类
 * @date: 2026-03-05
 */
@Slf4j
@Service
public class DictService extends ServiceImpl<DictRepository, Dict> {

    /**
     * 字典数据访问层接口对象
     */
    private final DictRepository dictRepository;
    /**
     * 字典数据访问层接口对象
     */
    private final DictMapper dictMapper;
    /**
     * 字典类型业务逻辑类对象
     */
    private final DictTypeService dictTypeService;

    /**
     * 构造函数 - 向 Spring IOC 容器中注入字典数据访问层接口对象
     *
     * @param dictRepository  字典数据访问层接口对象
     * @param dictMapper      字典数据访问层接口对象
     * @param dictTypeService 字典类型业务逻辑类对象
     */
    @Autowired
    public DictService(DictRepository dictRepository, DictMapper dictMapper, DictTypeService dictTypeService) {
        this.dictRepository = dictRepository;
        this.dictMapper = dictMapper;
        this.dictTypeService = dictTypeService;
    }

    /**
     * 新增一条字典数据
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveOneDict(DictDTO dictDTO) {
        verifyDictParams(dictDTO, OperateTypeEnum.SAVE);
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getDictTypeId, dictDTO.getDictTypeId()).eq(Dict::getCode, dictDTO.getCode());
        Long count = dictRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("新增字典数据时, 字典数据 {} - {} - {} 已存在!", dictDTO.getDictTypeId(), dictDTO.getName(), dictDTO.getCode());
            throw new ServiceException("新增字典数据时, 字典数据 %s - %s -%s 已存在!".formatted(dictDTO.getDictTypeId(), dictDTO.getName(), dictDTO.getCode()));
        }
        Dict dict = dictMapper.dictDTOToDict(dictDTO);
        dict.setCreateBy(StpUtil.getLoginIdAsString());
        dict.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean saveResult = saveOrUpdate(dict);
        if (!saveResult) {
            log.error("新增字典数据保存失败!");
            throw new ServiceException("新增字典数据保存失败!");
        }
    }

    /**
     * 删除一条字典数据
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteOneDict(DictDTO dictDTO) {
        verifyDictParams(dictDTO, OperateTypeEnum.DELETE);
        Dict dict = dictMapper.dictDTOToDict(dictDTO);
        dict.setUpdateBy(StpUtil.getLoginIdAsString());
        boolean removeResult = removeById(dict);
        if (!removeResult) {
            log.error("字典数据删除失败!");
            throw new ServiceException("字典数据删除失败!");
        }
    }

    /**
     * 修改一条字典数据
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateOneDict(DictDTO dictDTO) {
        verifyDictParams(dictDTO, OperateTypeEnum.UPDATE);
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Dict::getDictTypeId, dictDTO.getDictTypeId()).eq(Dict::getCode, dictDTO.getCode());
        Long count = dictRepository.selectCount(queryWrapper);
        if (count > 0) {
            log.error("修改字典数据时, 字典数据 {} - {} - {} 已存在!", dictDTO.getDictTypeId(), dictDTO.getName(), dictDTO.getCode());
            throw new ServiceException("修改字典数据时, 字典数据 %s - %s -%s 已存在!".formatted(dictDTO.getDictTypeId(), dictDTO.getName(), dictDTO.getCode()));
        }
        Dict dict = dictRepository.selectOneDictById(dictDTO.getId());
        Optional.ofNullable(dictDTO.getDictTypeId()).filter(StrUtil::isNotBlank).ifPresent(dict::setDictTypeId);
        Optional.ofNullable(dictDTO.getName()).filter(StrUtil::isNotBlank).ifPresent(dict::setName);
        Optional.ofNullable(dictDTO.getCode()).filter(StrUtil::isNotBlank).ifPresent(dict::setCode);
        Optional.ofNullable(dictDTO.getValue()).filter(StrUtil::isNotBlank).ifPresent(dict::setValue);
        Optional.ofNullable(dictDTO.getStatus()).filter(item -> !Objects.equals(item, dict.getStatus())).ifPresent(dict::setStatus);
        Optional.ofNullable(dictDTO.getRemark()).filter(StrUtil::isNotBlank).ifPresent(dict::setRemark);
        dict.setUpdateBy(StpUtil.getLoginIdAsString());
        Boolean updateResult = dictRepository.updateOneDictById(dict);
        if (!updateResult) {
            log.error("修改字典数据修改失败!");
            throw new ServiceException("修改字典数据修改失败!");
        }
    }

    /**
     * 查询字典数据列表
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     * @return 字典数据列表 - {@link PageResponse} - {@link DictDTO}
     */
    public PageResponse<DictDTO> queryDictList(DictDTO dictDTO) {
        IPage<Dict> page = PageUtils.buildPage(dictDTO);
        LambdaQueryWrapper<Dict> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StrUtil.isNotBlank(dictDTO.getDictTypeId()), Dict::getDictTypeId, dictDTO.getDictTypeId());
        queryWrapper.like(StrUtil.isNotBlank(dictDTO.getName()), Dict::getName, dictDTO.getName());
        queryWrapper.like(StrUtil.isNotBlank(dictDTO.getCode()), Dict::getCode, dictDTO.getCode());
        queryWrapper.like(StrUtil.isNotBlank(dictDTO.getValue()), Dict::getValue, dictDTO.getValue());
        IPage<Dict> dictPage = page(page, queryWrapper);
        return PageUtils.buildPageResponse(dictPage, dictMapper.dictListToDictDTOList(dictPage.getRecords()));
    }

    /**
     * 通过字典类型 CODE 查询字典数据列表
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}, 此处主要使用 {@link DictDTO#getDictTypeCode()} 属性
     * @return 字典数据列表 - {@link List} - {@link DictDTO}
     */
    public List<DictDTO> queryDictByTypeCode(DictDTO dictDTO) {
        LambdaQueryWrapper<DictType> dictTypeQueryWrapper = new LambdaQueryWrapper<>();
        dictTypeQueryWrapper.eq(DictType::getCode, dictDTO.getDictTypeCode());
        DictType dictType = dictTypeService.getOne(dictTypeQueryWrapper);
        if (Objects.isNull(dictType) || StrUtil.isBlank(dictType.getId())) {
            log.error("通过字典类型 CODE {} 查询对应的字典数据失败, 未能获取到对应的字典类型数据!", dictDTO.getDictTypeCode());
            throw new ServiceException("通过字典类型 CODE %s 查询对应的字典数据失败, 未能获取到对应的字典类型数据!".formatted(dictDTO.getDictTypeCode()));
        }
        LambdaQueryWrapper<Dict> dictQueryWrapper = new LambdaQueryWrapper<>();
        dictQueryWrapper.eq(Dict::getDictTypeId, dictType.getId());
        List<Dict> dictList = this.dictRepository.selectList(dictQueryWrapper);
        return dictMapper.dictListToDictDTOList(dictList);
    }

    /**
     * 字典数据参数校验
     *
     * @param dictDTO     字典 DTO 对象, {@link DictDTO}
     * @param operateType 操作类型, {@link OperateTypeEnum}
     */
    private void verifyDictParams(DictDTO dictDTO, OperateTypeEnum operateType) {
        switch (operateType) {
            case SAVE -> verifySaveDictParams(dictDTO);
            case DELETE -> verifyDeleteDictParams(dictDTO);
            case BATCH_DELETE -> verifyBatchDeleteDictParams(dictDTO);
            case UPDATE -> verifyUpdateDictParams(dictDTO);
            default -> throw new ServiceException("不支持的操作类型!");
        }
    }

    /**
     * 字典数据参数校验 - 新增单条字典数据参数验证
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    private void verifySaveDictParams(DictDTO dictDTO) {
        if (StrUtil.isBlank(dictDTO.getDictTypeId())) {
            log.error("新增字典数据失败，字典类型 ID 不能为空!");
            throw new ServiceException("新增字典数据失败，字典类型 ID 不能为空!");
        }
        if (StrUtil.isBlank(dictDTO.getName())) {
            log.error("新增字典数据失败，字典值名称不能为空!");
            throw new ServiceException("新增字典数据失败，字典值名称不能为空!");
        }
        if (StrUtil.isBlank(dictDTO.getCode())) {
            log.error("新增字典数据失败，字典值编码不能为空!");
            throw new ServiceException("新增字典数据失败，字典值编码不能为空!");
        }
        if (StrUtil.isBlank(dictDTO.getValue())) {
            log.error("新增字典数据失败，字典值不能为空!");
            throw new ServiceException("新增字典数据失败，字典值不能为空!");
        }
    }

    /**
     * 字典数据参数校验 - 删除单条字典数据参数验证
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    private void verifyDeleteDictParams(DictDTO dictDTO) {
        if (StrUtil.isBlank(dictDTO.getId())) {
            log.error("删除字典数据失败，数据 ID 不能为空!");
            throw new ServiceException("删除字典数据失败，数据 ID 不能为空!");
        }
    }

    /**
     * 字典数据参数校验 - 批量删除字典数据参数验证
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    private void verifyBatchDeleteDictParams(DictDTO dictDTO) {
        if (CollectionUtil.isEmpty(dictDTO.getIds())) {
            log.error("批量删除字典数据失败，数据 ID 列表不能为空!");
            throw new ServiceException("批量删除字典数据失败，数据 ID 列表不能为空!");
        }
    }

    /**
     * 字典数据参数校验 - 修改单条字典数据参数验证
     *
     * @param dictDTO 字典 DTO 对象, {@link DictDTO}
     */
    private void verifyUpdateDictParams(DictDTO dictDTO) {
        if (StrUtil.isBlank(dictDTO.getId())) {
            log.error("修改字典数据失败，数据 ID 不能为空!");
            throw new ServiceException("修改字典数据失败，数据 ID 不能为空!");
        }
    }
}
