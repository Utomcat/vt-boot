package com.ranyk.vt.boot.example.satoken.domain.dict.po;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeleteDictPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 前端传入的删除字典值参数封装 PO 类
 * @date: 2026-03-05
 */
@Data
@ToString
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DeleteDictPO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4543294345174563159L;

    /**
     * 字典值ID
     */
    private String id;
    /**
     * 字典值ID列表
     */
    private List<String> ids;
}
