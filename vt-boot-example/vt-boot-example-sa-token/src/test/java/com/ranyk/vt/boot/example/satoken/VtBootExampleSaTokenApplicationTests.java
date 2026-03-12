package com.ranyk.vt.boot.example.satoken;

import cn.hutool.core.util.RandomUtil;
import com.ranyk.vt.boot.base.exception.ServiceException;
import com.ranyk.vt.boot.base.response.PageResponse;
import com.ranyk.vt.boot.cache.util.CacheUtils;
import com.ranyk.vt.boot.example.satoken.domain.account.dto.AccountDTO;
import com.ranyk.vt.boot.example.satoken.domain.captcha.dto.CaptchaDTO;
import com.ranyk.vt.boot.example.satoken.service.account.AccountService;
import com.ranyk.vt.boot.example.satoken.service.captcha.CaptchaService;
import com.ranyk.vt.boot.example.satoken.service.login.LonginService;
import com.ranyk.vt.boot.example.satoken.service.logout.LogoutService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
     * 登录业务逻辑对象
     */
    @Autowired
    private LonginService longinService;
    /**
     * 登出业务逻辑对象
     */
    @Autowired
    private LogoutService logoutService;

    /**
     * 每个测试方法之前执行的方法 - 用户登录
     */
    @BeforeEach
    @DisplayName("每个测试方法之前执行的方法 - 用户登录")
    void login(){
        CaptchaDTO captcha = captchaService.captcha();
        AccountDTO accountDTO = AccountDTO.builder().userName("admin").password("123456").captcha(captcha.getCaptcha()).build();
        Assertions.assertAll(() -> longinService.login(accountDTO));
    }

    /**
     * 每个测试方法之后执行方法 - 用户登出
     */
    @AfterEach
    @DisplayName("每个测试方法之后执行方法 - 用户登出")
    void logout(){
        logoutService.logout();
    }

    /**
     * 测试缓存功能 - 使用默认的 Caffeine 缓存
     */
    @Test
    @DisplayName("测试缓存功能 - 使用默认的 Caffeine 缓存")
    void caffeineCache() {
        CacheUtils.cache("test", "test");
        log.debug("缓存数据: {}", CacheUtils.getCache("test"));
        log.debug("缓存完数据后, 缓存是否存在: {}", CacheUtils.exists("test"));
        String cacheStats = CacheUtils.getCacheStats();
        log.debug("缓存统计信息: {}", cacheStats);
        Boolean deleteResult = CacheUtils.deleteCache("test");
        log.debug("删除缓存数据结果: {}", deleteResult);
        log.debug("删除缓存数据后, 缓存是否存在: {}", CacheUtils.exists("test"));
    }

    /**
     * 测试缓存功能 - 使用 Redis 缓存
     */
    @Test
    @DisplayName("测试缓存功能 - 使用 Redis 缓存")
    void redisCache() {
        CacheUtils.cacheWithRedis("test", "test");
        log.debug("使用 Redis 缓存数据: {}", CacheUtils.getCacheWithRedis("test"));
        log.debug("使用 Redis 缓存完数据后, 缓存是否存在: {}", CacheUtils.existsWithRedis("test"));
        Boolean deleteResult = CacheUtils.deleteCacheWithRedis("test");
        log.debug("使用 Redis 删除缓存数据结果: {}", deleteResult);
        log.debug("使用 Redis 删除缓存数据后, 缓存是否存在: {}", CacheUtils.existsWithRedis("test"));
    }

    /**
     * 验证码生成功能测试
     */
    @Test
    @DisplayName("验证码生成功能测试")
    void captchaGenerateTest() {
        CaptchaDTO captcha = captchaService.captcha();
        log.debug("生成的验证码是: {}", captcha);
        try {
            log.debug("本地缓存中的验证码是: {}", CacheUtils.getCache(captcha.getCaptcha() + ":captcha"));
        } catch (Exception e) {
            log.error("本地缓存中的验证码获取失败, 异常信息为: {} , 异常栈为: ", e.getMessage(), e);
        }
        try {
            log.debug("Redis 缓存中的验证码是: {}", CacheUtils.getCacheWithRedis(captcha.getCaptcha() + ":captcha"));
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
        ServiceException exception = Assertions.assertThrows(ServiceException.class, () -> accountService.saveOneAccount(userDTO), "期望抛出 ServiceException 异常, 异常信息为: 账户名已存在");

        // 日志记录
        log.debug("捕获到预期异常：{}", exception.getMessage());

        // 验证异常信息是否符合预期
        Assertions.assertEquals("user.username.exists", exception.getCode(), "异常 code 应包是 'user.username.exists' 错误码");
    }

    @Test
    @DisplayName("添加账户信息功能测试 - 新增一条不存在于系统内的账户名的账户")
    void addAccountTestNotSameUserName() {
        // 创建账户信息数据传输对象
        AccountDTO userDTO = AccountDTO.builder().userName(RandomUtil.randomNumbers(6)).password("123456").build();

        // 添加账户信息
        Assertions.assertAll(() -> accountService.saveOneAccount(userDTO));

        // 查询账户信息
        Assertions.assertTrue(() -> {
            PageResponse<AccountDTO> accountDTOPageResponse = accountService.queryAccountByConditions(userDTO);
            return accountDTOPageResponse.getData().stream().anyMatch(item -> item.getUserName().equals(userDTO.getUserName()));
        }, "新增的一个不存在于系统内的账户名的账户, 保存失败!");
    }

    /**
     * 删除账户信息功能测试
     */
    @Test
    @DisplayName("删除账户信息功能测试 - 删除一条存在于系统内的账户信息方法2")
    void deleteAccountTest2() {
        AccountDTO accountDTO = AccountDTO.builder().id("").build();
        Assertions.assertAll(() -> accountService.deleteOneAccount2(accountDTO));
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
    @DisplayName("查询账户信息功能测试 - 查询一条存在于系统内的账户 (模糊查询)")
    void queryAccountTest() {
        PageResponse<AccountDTO> accountDTOPageResponse = accountService.queryAccountByConditions(AccountDTO.builder().userName("admin").build());
        log.debug("查询结果为: {}", accountDTOPageResponse);
    }
}
