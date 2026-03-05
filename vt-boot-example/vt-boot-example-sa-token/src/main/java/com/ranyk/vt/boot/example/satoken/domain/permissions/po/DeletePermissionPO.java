package com.ranyk.vt.boot.example.satoken.domain.permissions.po;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * CLASS_NAME: DeletePermissionPO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 删除权限信息参数对象 PO 类
 * @date: 2026-03-05
 */
@Data
@Builder
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
public class DeletePermissionPO implements Serializable {
    @Serial
    private static final long serialVersionUID = -6010303360550230170L;

    /**
     * 权限ID
     */
    private String id;
    /**
     * 权限ID列表
     */
    private List<String> ids;
}
