package com.ranyk.vt.boot.example.web.freamwork.domain.captcha.vo;

import jakarta.annotation.Nonnull;
import lombok.Builder;

import java.io.Serializable;

/**
 * CLASS_NAME: CaptchaVO.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码请求返回前端数据封装类, 字段说明: <br/>
 * <ul>
 *     <li>captchaKey: 验证码缓存 KEY</li>
 *     <li>captcha: 验证码值, 不能为空</li>
 * @date: 2026-03-12
 */
@Builder
public record CaptchaVO(String captchaKey, @Nonnull String captcha) implements Serializable {

}
