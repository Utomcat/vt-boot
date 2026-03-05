package com.ranyk.vt.boot.example.satoken.domain.dict.vo;

import com.ranyk.vt.boot.base.domain.vo.BaseVO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: DictVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典对象返回前端视图VO
 * @date: 2026-03-05
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true)
@EqualsAndHashCode(callSuper=true)
public class DictVO extends BaseVO {
    @Serial
    private static final long serialVersionUID = 842439900493232052L;

    /**
     * 字典类型ID
     */
    private String dictTypeId;
    /**
     * 字典编码
     */
    private String code;
    /**
     * 字典名称
     */
    private String name;
    /**
     * 字典值
     */
    private String value;
}
