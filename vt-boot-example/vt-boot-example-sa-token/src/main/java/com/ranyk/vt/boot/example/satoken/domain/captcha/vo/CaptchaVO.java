package com.ranyk.vt.boot.example.satoken.domain.captcha.vo;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * CLASS_NAME: CaptchaVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码请求返回前端数据封装类
 * @date: 2026-03-01
 */
@Data
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class CaptchaVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -356017333387044335L;

    /**
     * 验证码
     */
    private String captcha;
}
