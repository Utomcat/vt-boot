package com.ranyk.vt.boot.example.satoken.service.captcha;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ranyk.vt.boot.base.constant.CacheTypeEnum;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.cache.config.properties.CacheConfigurationProperties;
import com.ranyk.vt.boot.cache.util.CacheUtils;
import com.ranyk.vt.boot.example.satoken.domain.captcha.dto.CaptchaDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

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
     * 缓存配置属性对象
     */
    private final CacheConfigurationProperties cacheConfigurationProperties;

    /**
     * 构造方法 - 向 Spring IOC 容器中注入缓存配置属性对象
     *
     * @param cacheConfigurationProperties 缓存配置属性对象
     */
    @Autowired
    public CaptchaService(CacheConfigurationProperties cacheConfigurationProperties) {
        this.cacheConfigurationProperties = cacheConfigurationProperties;
    }

    /**
     * 获取验证码
     *
     * @return 验证码字符串
     */
    public CaptchaDTO captcha() {
        // 生成验证码
        String captcha = RandomUtil.randomNumbers(6);
        // 生成验证码缓存 KEY, 构成规则为 验证码值 + ":captcha"
        String captchaKey = captcha+":captcha";
        // 判断是否开启缓存功能, 如果未开启, 则抛出异常
        if (!cacheConfigurationProperties.getEnabled()) {
            log.error("缓存功能未启用, 无法进行验证码缓存!");
            throw new ServiceException("缓存功能未启用, 无法进行验证码缓存!");
        }
        // 判断是否开启 Caffeine 缓存功能, 如果开启, 则缓存验证码到 Caffeine 本地缓存
        if (cacheConfigurationProperties.getIsCaffeineEnabled()) {
            CacheUtils.cache(captchaKey, captcha);
        }
        // 判断是否开启 Redis 缓存功能, 如果开启, 则缓存验证码到 Redis 缓存
        if (cacheConfigurationProperties.getIsRedisEnabled()) {
            try {
                CacheUtils.cacheWithRedis(captchaKey, captcha);
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }
        }
        log.trace("验证码生成成功, 验证码为 {} !", captcha);
        // 返回验证码
        return CaptchaDTO.builder().captcha(captcha).build();
    }

    /**
     * 验证码验证 - 验证成功后自动删除缓存中的验证码
     *
     * @param captcha 验证码字符串
     * @return 验证结果
     */
    public Boolean verifyCaptcha(String captchaKey, String captcha) {
        // 定义验证码验证结果局部变量, 默认值为 FALSE
        Boolean verifyResult = Boolean.FALSE;
        // 获取验证码缓存 KEY, 如果未提供, 则默认使用 验证码值 + ":captcha" 作为缓存 KEY
        captchaKey = Optional.ofNullable(captchaKey).filter(StrUtil::isNotBlank).orElse(captcha+":captcha");
        // 判断是否开启缓存功能
        if (!cacheConfigurationProperties.getEnabled()) {
            throw new ServiceException("缓存功能未启用, 验证码无法进行验证!");
        }
        // 如果开启本地 Caffeine 缓存, 则先验证本地缓存中的验证码, 并验证成功后自动删除
        if (cacheConfigurationProperties.getIsCaffeineEnabled()) {
            verifyResult = verifyAndDeleteCaptcha(captcha, captchaKey, CacheTypeEnum.CAFFEINE);
        }
        // 如果开启 Redis 缓存, 则进行 Redis 缓存中的验证码验证, 并验证成功后自动删除
        if (cacheConfigurationProperties.getIsRedisEnabled()) {
            verifyResult = verifyAndDeleteCaptcha(captcha, captchaKey, CacheTypeEnum.REDIS);
        }
        // 返回验证结果
        return verifyResult;
    }

    /**
     * 抽象验证码验证与删除逻辑
     *
     * @param captcha   用户输入的验证码
     * @param cacheKey  缓存键
     * @param cacheType 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 验证结果
     */
    private Boolean verifyAndDeleteCaptcha(String captcha, String cacheKey, CacheTypeEnum cacheType) {
        // 检查缓存中是否存在验证码
        if (!existsInCache(cacheKey, cacheType)) {
            log.error("缓存中不存在验证码, 直接返回验证结果为 FALSE !");
            return Boolean.FALSE;
        }

        // 获取缓存中的验证码
        String cachedCaptcha = getFromCache(cacheKey, cacheType);
        // 验证码验证, 匹配成功则删除缓存
        Boolean verifyResult = Objects.equals(cachedCaptcha, captcha);
        log.trace("缓存中存在验证码, 验证结果为 {} !", verifyResult);
        // 验证失败直接返回
        if (!verifyResult) {
            log.error("验证码验证未通过, 直接返回验证结果为 FALSE !");
            return Boolean.FALSE;
        }
        // 验证成功后删除缓存
        if (!deleteFromCache(cacheKey, cacheType)) {
            log.error("验证码验证通过后, 删除缓存失败, 直接返回 FALSE !");
            return Boolean.FALSE;
        }
        log.trace("验证码验证通过且删除成功 !");
        return Boolean.TRUE;
    }

    /**
     * 检查缓存中是否存在指定键
     *
     * @param key       缓存键
     * @param cacheType 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 是否存在
     */
    private boolean existsInCache(String key, CacheTypeEnum cacheType) {
        return switch (cacheType) {
            case CAFFEINE -> CacheUtils.exists(key);
            case REDIS -> CacheUtils.existsWithRedis(key);
        };
    }

    /**
     * 从缓存中获取指定键的值
     *
     * @param key       缓存键
     * @param cacheType 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 缓存值
     */
    private String getFromCache(String key, CacheTypeEnum cacheType) {
        return switch (cacheType) {
            case CAFFEINE -> String.valueOf(CacheUtils.getCache(key));
            case REDIS -> String.valueOf(CacheUtils.getCacheWithRedis(key));
        };
    }

    /**
     * 从缓存中删除指定键
     *
     * @param key       缓存键
     * @param cacheType 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 删除结果
     */
    private boolean deleteFromCache(String key, CacheTypeEnum cacheType) {
        return switch (cacheType) {
            case CAFFEINE -> CacheUtils.deleteCache(key);
            case REDIS -> CacheUtils.deleteCacheWithRedis(key);
        };
    }
}
