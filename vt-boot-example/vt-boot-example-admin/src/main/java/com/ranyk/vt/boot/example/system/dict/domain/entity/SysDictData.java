package com.ranyk.vt.boot.example.system.dict.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ranyk.vt.boot.base.domain.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: SysDictData.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典数据实体, 对应表 t_dict_data
 * @date: 2026-11-20
 */
@Data
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("t_dict_data")
public class SysDictData extends BaseEntity {

    @Serial
    private static final long serialVersionUID = -2241601577456253376L;

    /**
     * 所属字典类型编码, 关联 t_dict_type.dict_type
     */
    private String dictType;
    /**
     * 字典标签, 展示给用户看的中文描述, 如 正常
     */
    private String dictLabel;
    /**
     * 字典键值, 实际存储与业务判断使用的值, 如 1
     */
    private String dictValue;
    /**
     * 排序值, 越小越靠前
     */
    private Integer dictSort;
    /**
     * 标签样式(Element Plus 标签类型): primary / success / warning / danger / info
     */
    private String tagType;
    /**
     * 额外样式类名, 供前端自定义渲染使用
     */
    private String cssClass;
    /**
     * 是否默认选中: 1-是 0-否
     */
    private Integer isDefault;
    /**
     * 状态: 1-正常 0-禁用
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
}
