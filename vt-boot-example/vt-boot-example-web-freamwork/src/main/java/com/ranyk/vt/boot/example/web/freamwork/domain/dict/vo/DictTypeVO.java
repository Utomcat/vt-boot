package com.ranyk.vt.boot.example.web.freamwork.domain.dict.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: DictTypeVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型返回前端视图VO, 字段说明: <br/>
 * <ul>
 *     <li>id: 字典类型ID</li>
 *     <li>name: 字典类型名称</li>
 *      <li>code: 字典类型编码</li>
 *      <li>status: 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;(默认值)）</li>
 *      <li>remark: 字典类型备注</li>
 * </ul>
 * @date: 2026-03-12
 */
@Builder
public record DictTypeVO(@Nonnull String id, @Nonnull String name, @Nonnull String code, Integer status, String remark) implements Serializable {

}
