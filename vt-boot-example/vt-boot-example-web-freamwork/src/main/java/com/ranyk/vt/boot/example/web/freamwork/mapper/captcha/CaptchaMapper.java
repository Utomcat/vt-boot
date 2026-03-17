package com.ranyk.vt.boot.example.web.freamwork.mapper.captcha;

import com.ranyk.vt.boot.example.web.freamwork.domain.captcha.dto.CaptchaDTO;
import com.ranyk.vt.boot.example.web.freamwork.domain.captcha.vo.CaptchaVO;
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
     * 验证码DTO 转 VO
     *
     * @param captchaDTO 验证码DTO {@link CaptchaDTO}
     * @return 验证码VO 视图对象 {@link CaptchaVO}
     */
    CaptchaVO captchaDTOToVO(CaptchaDTO captchaDTO);
}
