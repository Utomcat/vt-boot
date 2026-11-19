package com.ranyk.vt.boot.example.system.dict.reader;

import java.util.List;

/**
 * CLASS_NAME: DictReader.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典读取能力契约。业务侧统一通过本接口读取某字典类型下的字典项, 不直接接触持久层与缓存实现;
 * 该接口只依赖 {@link DictItem}, 不引任何框架实现类型, 作为"读取侧"与"管理侧"分离的接缝,
 * 后续如需下沉为独立 starter, 可整体迁移而业务调用方无感
 * @date: 2026-11-20
 */
public interface DictReader {

    /**
     * 按字典类型编码读取全部启用的字典项, 按排序值与主键升序
     *
     * @param dictType 字典类型编码
     * @return 字典项列表, 无数据时返回空列表
     */
    List<DictItem> listByType(String dictType);

    /**
     * 按字典类型编码与键值读取单个字典项
     *
     * @param dictType 字典类型编码
     * @param value    字典键值
     * @return 命中的字典项, 未命中返回 null
     */
    DictItem getByValue(String dictType, String value);

    /**
     * 按字典类型编码与键值读取字典标签
     *
     * @param dictType 字典类型编码
     * @param value    字典键值
     * @return 字典标签, 未命中返回 null
     */
    String getLabel(String dictType, String value);

    /**
     * 失效指定字典类型的读取缓存。管理侧发生写入(新增/修改/删除/启停)或类型变更时调用
     *
     * @param dictType 字典类型编码
     */
    void evict(String dictType);
}
