package com.ranyk.vt.boot.example.web.freamwork.api.captcha;


import com.ranyk.vt.boot.example.web.freamwork.domain.captcha.vo.CaptchaVO;
import com.ranyk.vt.boot.example.web.freamwork.mapper.captcha.CaptchaMapper;
import com.ranyk.vt.boot.example.web.freamwork.service.captcha.CaptchaService;
import com.ranyk.vt.boot.log.annotations.Log;
import com.ranyk.vt.boot.web.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * CLASS_NAME: CaptchaApi.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码请求接口 API
 * @date: 2026-03-01
 */
@RestController
@RequestMapping("/api/captcha")
public class CaptchaApi {

    /**
     * 验证码业务逻辑类对象
     */
    private final CaptchaService captchaService;
    /**
     * 验证码实体转换接口对象
     */
    private final CaptchaMapper captchaMapper;

    /**
     * 构造方法 - 向 Spring IOC 容器中自动注入 CaptchaService 对象
     *
     * @param captchaService 验证码业务逻辑类对象, {@link CaptchaService}
     * @param captchaMapper  验证码实体转换接口对象, {@link CaptchaMapper}
     */
    @Autowired
    public CaptchaApi(CaptchaService captchaService, CaptchaMapper captchaMapper) {
        this.captchaService = captchaService;
        this.captchaMapper = captchaMapper;
    }

    /**
     * 获取验证码接口
     *
     * @return 验证码字符串
     */
    @GetMapping
    @Log(operation = "获取验证码请求", type = Log.LogType.ACCESS)
    public Result<CaptchaVO> captcha() {
        return Result.success(captchaMapper.captchaDTOToVO(captchaService.captcha()));
    }
}
