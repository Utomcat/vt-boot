package com.ranyk.vt.boot.example.system.dict.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;

/**
 * CLASS_NAME: SysDictDataMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据表 Mapper
 * @date: 2026-11-20
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {
}
