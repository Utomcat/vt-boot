package com.ranyk.vt.boot.example.system.dict.reader;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: DictItem.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 字典读取侧的轻量字典项, 与持久层解耦, 作为 {@link DictReader} 的输出模型;
 * 后续将该能力下沉为框架模块时, 本模型可原样迁移, 不依赖 MyBatis-Plus 等实现细节
 * @date: 2026-11-20
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class DictItem implements Serializable {

    @Serial
    private static final long serialVersionUID = -7953258611497320451L;

    /**
     * 字典类型编码
     */
    private String dictType;
    /**
     * 字典标签(展示文本)
     */
    private String label;
    /**
     * 字典键值(存储与判断使用)
     */
    private String value;
    /**
     * 排序值, 越小越靠前
     */
    private Integer sort;
    /**
     * 标签样式: primary/success/warning/danger/info
     */
    private String tagType;
    /**
     * 额外样式类名
     */
    private String cssClass;
    /**
     * 是否默认选中: 1-是 0-否
     */
    private Integer isDefault;
}
