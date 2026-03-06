package com.ranyk.vt.boot.example.satoken.repository.dict;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.dict.entity.DictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: DictRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型数据访问层接口
 * @date: 2026-03-05
 */
@Mapper
@Component
public interface DictTypeRepository extends BaseMapper<DictType> {

    /**
     * 根据 ID 查询一条字典类型数据
     *
     * @param id 字典类型 ID
     * @return 字典类型数据
     */
    DictType selectOneDictTypeById(@Param("id") String id);

    /**
     * 根据 ID 更新一条字典类型数据
     *
     * @param dictType 字典类型数据封装对象, {@link DictType}
     * @return 更新结果, true: 表示更新成功; false: 表示更新失败;
     */
    Boolean updateOneDictTypeById(@Param("dictType") DictType dictType);
}