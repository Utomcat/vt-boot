package com.ranyk.vt.boot.example.system.dict.reader.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.ranyk.vt.boot.cache.util.CacheUtils;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictData;
import com.ranyk.vt.boot.example.system.dict.domain.entity.SysDictType;
import com.ranyk.vt.boot.example.system.dict.mapper.SysDictDataMapper;
import com.ranyk.vt.boot.example.system.dict.mapper.SysDictTypeMapper;
import com.ranyk.vt.boot.example.system.dict.reader.DictItem;
import com.ranyk.vt.boot.example.system.dict.reader.DictReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * CLASS_NAME: CachedDictReader.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 带缓存的字典读取实现。读取时优先命中本地/Redis 缓存, 未命中则查库并回填;
 * 缓存异常时自动降级为直接查库, 不影响主业务。管理侧的写操作通过 {@link #evict(String)} 精确失效对应字典类型。
 * 读取前会校验字典类型: 类型不存在或已被禁用时整类不可读, 直接返回空列表。
 * <p>
 * 缓存键规范: {@code vt:dict:data:{dictType}} (遵循 {应用}:{模块}:{业务}:{标识} 约定)。
 * </p>
 * @date: 2026-11-20
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CachedDictReader implements DictReader {

    /**
     * 状态: 正常
     */
    private static final int STATUS_NORMAL = 1;
    /**
     * 字典读取缓存键前缀
     */
    private static final String CACHE_KEY_PREFIX = "vt:dict:data:";
    /**
     * 缓存未命中时 CacheUtils 返回的占位值
     */
    @SuppressWarnings("unused")
    private static final String CACHE_MISS_PLACEHOLDER = "-";

    /**
     * 字典数据数据访问对象
     */
    private final SysDictDataMapper sysDictDataMapper;
    /**
     * 字典类型数据访问对象, 用于校验字典类型是否存在且启用
     */
    private final SysDictTypeMapper sysDictTypeMapper;

    @Override
    public List<DictItem> listByType(String dictType) {
        if (StrUtil.isBlank(dictType)) {
            return List.of();
        }
        List<DictItem> cached = readFromCache(dictType);
        if (cached != null) {
            return cached;
        }
        List<DictItem> items = loadFromDb(dictType);
        writeToCache(dictType, items);
        return items;
    }

    @Override
    public DictItem getByValue(String dictType, String value) {
        if (StrUtil.isBlank(value)) {
            return null;
        }
        for (DictItem item : listByType(dictType)) {
            if (StrUtil.equals(item.getValue(), value)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public String getLabel(String dictType, String value) {
        DictItem item = getByValue(dictType, value);
        return item == null ? null : item.getLabel();
    }

    @Override
    public void evict(String dictType) {
        if (StrUtil.isBlank(dictType)) {
            return;
        }
        try {
            CacheUtils.deleteCache(CACHE_KEY_PREFIX + dictType);
        } catch (Exception e) {
            log.warn("失效字典缓存失败, dictType: {}, 异常 => ", dictType, e);
        }
    }

    /**
     * 从缓存读取字典项列表
     *
     * @param dictType 字典类型编码
     * @return 命中的字典项列表; 未命中或缓存不可用时返回 null
     */
    @SuppressWarnings("unchecked")
    private List<DictItem> readFromCache(String dictType) {
        try {
            Object cached = CacheUtils.getCache(CACHE_KEY_PREFIX + dictType);
            if (!(cached instanceof List<?>)) {
                return null;
            }
            return new ArrayList<>((List<DictItem>) cached);
        } catch (Exception e) {
            log.warn("读取字典缓存失败, 降级为直接查库, dictType: {}, 异常 => ", dictType, e);
            return null;
        }
    }

    /**
     * 将字典项列表写入缓存
     *
     * @param dictType 字典类型编码
     * @param items    字典项列表
     */
    private void writeToCache(String dictType, List<DictItem> items) {
        try {
            CacheUtils.cache(CACHE_KEY_PREFIX + dictType, new ArrayList<>(items));
        } catch (Exception e) {
            log.warn("写入字典缓存失败, 不影响本次读取, dictType: {}, 异常 => ", dictType, e);
        }
    }

    /**
     * 从数据库加载指定类型下启用的字典项; 字典类型不存在或已被禁用时返回空列表(整类不可读)
     *
     * @param dictType 字典类型编码
     * @return 字典项列表
     */
    private List<DictItem> loadFromDb(String dictType) {
        SysDictType dictTypeEntity = sysDictTypeMapper.selectOne(
            Wrappers.<SysDictType>lambdaQuery().eq(SysDictType::getDictType, dictType));
        if (dictTypeEntity == null || !Integer.valueOf(STATUS_NORMAL).equals(dictTypeEntity.getStatus())) {
            return List.of();
        }
        return sysDictDataMapper.selectList(Wrappers.<SysDictData>lambdaQuery()
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getStatus, STATUS_NORMAL)
                .orderByAsc(SysDictData::getDictSort)
                .orderByAsc(SysDictData::getId))
            .stream()
            .map(this::toItem)
            .toList();
    }

    /**
     * 字典数据实体转读取侧字典项
     *
     * @param entity 字典数据实体
     * @return 字典项 {@link DictItem}
     */
    private DictItem toItem(SysDictData entity) {
        return DictItem.builder()
            .dictType(entity.getDictType())
            .label(entity.getDictLabel())
            .value(entity.getDictValue())
            .sort(entity.getDictSort())
            .tagType(entity.getTagType())
            .cssClass(entity.getCssClass())
            .isDefault(entity.getIsDefault())
            .build();
    }
}
