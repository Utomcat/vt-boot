package com.ranyk.vt.boot.example.satoken.mapper.captcha;

import com.ranyk.vt.boot.example.satoken.domain.captcha.dto.CaptchaDTO;
import com.ranyk.vt.boot.example.satoken.domain.captcha.vo.CaptchaVO;
import org.mapstruct.Mapper;

/**
 * CLASS_NAME: CaptchaMapper.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码实体转换接口
 * @date: 2026-03-02
 */
@Mapper(componentModel = "spring")
public interface CaptchaMapper {

    /**
     * 验证码数据传输类转换成验证码视图类
     *
     * @param captchaDTO 验证码数据传输类 {@link CaptchaDTO}
     * @return 验证码视图类 {@link CaptchaVO}
     */
    CaptchaVO dtoToVO(CaptchaDTO captchaDTO);
}
