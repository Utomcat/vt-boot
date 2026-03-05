package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteDictTypePO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的删除字典类型参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DeleteDictTypePO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5901837820421538435L;

    /**
     * 字典类型ID
     */
    private String id;
    /**
     * 字典类型ID列表
     */
    private List<String> ids;
}
