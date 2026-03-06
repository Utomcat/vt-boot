package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: UpdateDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的修改字典类型参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UpdateDictTypePO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2861496231675803487L;

    /**
     * 主键ID 雪花算法生成
     */
    private String id;
    /**
     * 字典类型名称
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;
    /**
     * 数据状态（-2: 其他非正常状态; -1: 删除/停用/无效; 0: 待启用; 1: 正常/有效/其他正常状态;）
     */
    @Builder.Default
    private Integer status = 1;
    /**
     * 字典类型备注
     */
    private String remark;
}
