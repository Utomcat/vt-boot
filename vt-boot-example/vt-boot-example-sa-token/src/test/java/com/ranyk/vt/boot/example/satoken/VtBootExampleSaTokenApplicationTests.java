package com.ranyk.vt.boot.example.satoken;

import com.ranyk.vt.boot.cache.util.CacheUtils;
import com.ranyk.vt.boot.example.satoken.domain.captcha.dto.CaptchaDTO;
import com.ranyk.vt.boot.example.satoken.service.captcha.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * CLASS_NAME: VtBootExampleSaTokenApplicationTests.java
 *
 * @author ranyk
 * @version V1.0
 * @description: SA-TOKEN - 样例测试启动类
 * @date: 2026-03-02
 */
@Slf4j
@SpringBootTest
class VtBootExampleSaTokenApplicationTests {

    @Autowired
    private CaptchaService captchaService;

    /**
     * 测试缓存功能 - 使用默认的 Caffeine 缓存
     */
    @Test
    void caffeineCache() {
        CacheUtils.cache("test", "test");
        log.info("缓存数据: {}", CacheUtils.getCache("test"));
        log.info("缓存完数据后, 缓存是否存在: {}", CacheUtils.exists("test"));
        String cacheStats = CacheUtils.getCacheStats();
        log.info("缓存统计信息: {}", cacheStats);
        Boolean deleteResult = CacheUtils.deleteCache("test");
        log.info("删除缓存数据结果: {}", deleteResult);
        log.info("删除缓存数据后, 缓存是否存在: {}", CacheUtils.exists("test"));
    }

    /**
     * 测试缓存功能 - 使用 Redis 缓存
     */
    @Test
    void redisCache() {
        CacheUtils.cacheWithRedis("test", "test");
        log.info("使用 Redis 缓存数据: {}", CacheUtils.getCacheWithRedis("test"));
        log.info("使用 Redis 缓存完数据后, 缓存是否存在: {}", CacheUtils.existsWithRedis("test"));
        Boolean deleteResult = CacheUtils.deleteCacheWithRedis("test");
        log.info("使用 Redis 删除缓存数据结果: {}", deleteResult);
        log.info("使用 Redis 删除缓存数据后, 缓存是否存在: {}", CacheUtils.existsWithRedis("test"));
    }

    /**
     * 验证码生成功能测试
     */
    @Test
    void captchaGenerateTest(){
        CaptchaDTO captcha = captchaService.captcha();
        log.info("生成的验证码是: {}", captcha);
        try {
            log.info("本地缓存中的验证码是: {}", CacheUtils.getCache(captcha.getCaptcha() + ":captcha"));
        } catch (Exception e) {
            log.error("本地缓存中的验证码获取失败, 异常信息为: {} , 异常栈为: ", e.getMessage(), e);
        }
        try {
            log.info("Redis 缓存中的验证码是: {}", CacheUtils.getCacheWithRedis(captcha.getCaptcha() + ":captcha"));
        } catch (Exception e) {
            log.error("Redis 缓存中的验证码获取失败, 异常信息为: {} , 异常栈为: ", e.getMessage(), e);
        }
    }
}
