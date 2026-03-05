package com.ranyk.vt.boot.example.satoken.domain.dict.vo;

import com.ranyk.vt.boot.base.domain.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: DictTypeVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典类型返回前端视图VO
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class DictTypeVO extends BaseVO {
    @Serial
    private static final long serialVersionUID = 3142745725814322190L;

    /**
     * 字典类型名称
     */
    private String name;
    /**
     * 字典类型编码
     */
    private String code;
}
