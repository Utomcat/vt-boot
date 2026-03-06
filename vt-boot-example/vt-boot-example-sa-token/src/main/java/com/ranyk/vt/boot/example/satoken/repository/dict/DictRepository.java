package com.ranyk.vt.boot.example.satoken.repository.dict;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.dict.entity.Dict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * CLASS_NAME: DictRepository.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据访问层接口
 * @date: 2026-03-05
 */
@Mapper
@Component
public interface DictRepository extends BaseMapper<Dict> {

    /**
     * 根据字典值 ID 查询一条字典数据
     *
     * @param id 字典值 ID
     * @return 字典数据 - {@link Dict}
     */
    Dict selectOneDictById(@Param("id") String id);

    /**
     * 根据字典值 ID 更新一条字典数据
     *
     * @param dict 字典数据 - {@link Dict}
     * @return 更新结果 - true: 成功; false: 失败
     */
    Boolean updateOneDictById(@Param("dict") Dict dict);
}