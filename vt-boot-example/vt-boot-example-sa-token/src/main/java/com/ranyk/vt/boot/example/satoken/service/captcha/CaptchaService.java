package com.ranyk.vt.boot.example.satoken.service.captcha;

import cn.hutool.core.util.RandomUtil;
import com.ranyk.vt.boot.log.annotations.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * CLASS_NAME: CaptchaService.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 验证码业务逻辑类
 * @date: 2026-03-01
 */
@Slf4j
@Service
public class CaptchaService {

    /**
     * 验证码缓存集合对象, 正式使用时不需要该对象
     */
    private static final Set<String> CAPTCHA_SET = new HashSet<>(100);

    /**
     * 获取验证码
     *
     * @return 验证码字符串
     */
    @Log(operation = "获取验证码", type = Log.LogType.OPERATION)
    public String captcha(){
        // 生成验证码
        String captcha = RandomUtil.randomNumbers(6);
        // 缓存验证码, TODO 正式使用时请使用缓存
        CAPTCHA_SET.add(captcha);
        return captcha;
    }

    /**
     * 验证码验证
     *
     * @param captcha 验证码字符串
     * @return 验证结果
     */
    public Boolean verifyCaptcha(String captcha){
        return CAPTCHA_SET.contains(captcha);
    }

    /**
     * 验证码删除
     *
     * @param captcha 验证码字符串
     */
    public void deleteCaptcha(String captcha){
        CAPTCHA_SET.remove(captcha);
    }

}
