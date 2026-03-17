package com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: DictVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典对象返回前端视图VO, 字段说明: <br/>
 * <ul>
 *     <li>id: 字典ID</li>
 *     <li>dictTypeId: 字典类型ID</li>
 *     <li>code: 字典编码</li>
 *     <li>name: 字典名称</li>
 *     <li>value: 字典值</li>
 *     <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态(默认值);）</li>
 *     <li>remark: 字典备注</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DictVO(@Nonnull String id, @Nonnull String dictTypeId, @Nonnull String code, @Nonnull String name, @Nonnull String value, Integer status, String remark) implements Serializable {

}
