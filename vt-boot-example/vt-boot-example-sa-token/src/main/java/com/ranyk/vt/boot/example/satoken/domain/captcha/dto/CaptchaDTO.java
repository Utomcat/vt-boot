package com.ranyk.vt.boot.example.satoken.domain.captcha.dto;

import com.ranyk.vt.boot.base.domain.dto.BaseDTO;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.Serial;

/**
 * CLASS_NAME: CaptchaDTO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码数据传输类
 * @date: 2026-03-01
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CaptchaDTO extends BaseDTO {
    @Serial
    private static final long serialVersionUID = 7047398975699171029L;

    /**
     * 验证码
     */
    private String captcha;
}
