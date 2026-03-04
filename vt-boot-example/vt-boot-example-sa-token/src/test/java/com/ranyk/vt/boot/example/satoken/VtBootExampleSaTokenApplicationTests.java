package com.ranyk.vt.boot.example.satoken;

import cn.hutool.core.util.RandomUtil;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.cache.util.CacheUtils;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.captcha.dto.CaptchaDTO;
import com.ranyk.vt.boot.example.satoken.service.account.AccountService;
import com.ranyk.vt.boot.example.satoken.service.captcha.CaptchaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

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

    /**
     * 验证码业务逻辑对象
     */
    @Autowired
    private CaptchaService captchaService;
    /**
     * 账户信息业务逻辑对象
     */
    @Autowired
    private AccountService accountService;

    /**
     * 测试缓存功能 - 使用默认的 Caffeine 缓存
     */
    @Test
    @DisplayName("测试缓存功能 - 使用默认的 Caffeine 缓存")
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
    @DisplayName("测试缓存功能 - 使用 Redis 缓存")
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
    @DisplayName("验证码生成功能测试")
    void captchaGenerateTest() {
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

    /**
     * 验证码验证功能测试
     */
    @Test
    @DisplayName("验证码验证功能测试")
    void captchaVerifyTest(){
        Assertions.assertTrue(captchaService.verifyCaptcha(null, "288846"), "验证码验证失败!");
    }

    /**
     * 添加账户信息功能测试 - 新增一条存在于系统内的账户名的账户
     */
    @Test
    @DisplayName("添加账户信息功能测试 - 新增一条存在于系统内的账户名的账户")
    void addAccountTestSameUserName() {
        // 创建账户信息数据传输对象
        AccountDTO userDTO = AccountDTO.builder().userName("admin").password("123456").build();

        // 添加账户信息, 期望抛出 ServiceException 异常, 异常信息为: 账户名已存在
        ServiceException exception = Assertions.assertThrows(ServiceException.class,
                () -> accountService.saveOne(userDTO),
                "期望抛出 ServiceException 异常, 异常信息为: 账户名已存在");

        // 日志记录
        log.info("捕获到预期异常：{}", exception.getMessage());

        // 验证异常信息是否符合预期
        Assertions.assertEquals("user.username.exists", exception.getCode(), "异常 code 应包是 'user.username.exists' 错误码");
    }

    @Test
    @DisplayName("添加账户信息功能测试 - 新增一条不存在于系统内的账户名的账户")
    void addAccountTestNotSameUserName() {
        // 创建账户信息数据传输对象
        AccountDTO userDTO = AccountDTO.builder().userName(RandomUtil.randomNumbers(6)).password("123456").build();

        // 添加账户信息
        Assertions.assertAll(() -> accountService.saveOne(userDTO));

        // 查询账户信息
        Assertions.assertTrue(() -> {
            AccountDTO accountDTO = accountService.queryAccountByConditions(userDTO);
            return Objects.equals(accountDTO.getUserName(), userDTO.getUserName());
        }, "新增的一个不存在于系统内的账户名的账户, 保存失败!");
    }

    /**
     * 删除账户信息功能测试
     */
    @Test
    @DisplayName("删除账户信息功能测试 - 删除一条存在于系统内的账户信息")
    void deleteAccountTest() {

    }

    /**
     * 修改账户信息功能测试
     */
    @Test
    @DisplayName("修改账户信息功能测试 - 修改一条存在于系统内的账户")
    void updateAccountTest() {

    }

    /**
     * 查询账户信息功能测试 - 查询一条存在于系统内的账户 (精确查询)
     */
    @Test
    @DisplayName("查询账户信息功能测试 - 查询一条存在于系统内的账户 (精确查询)")
    void queryAccountTest() {
        AccountDTO queryResult = accountService.queryAccountByConditions(AccountDTO.builder().userName("admin").build());
        log.info("查询结果为: {}", queryResult);
    }
}
