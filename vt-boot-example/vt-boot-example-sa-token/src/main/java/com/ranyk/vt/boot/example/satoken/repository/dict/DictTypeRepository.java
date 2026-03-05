package com.ranyk.vt.boot.example.satoken.repository.dict;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.satoken.domain.dict.entity.DictType;
import org.apache.ibatis.annotations.Mapper;
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

}