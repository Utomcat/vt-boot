package com.ranyk.vt.boot.base.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.BCrypt;
import cn.hutool.crypto.digest.MD5;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * CLASS_NAME: PasswordUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 密码编解码工具类
 * <p>
 * 新写入的密码统一使用 BCrypt 算法(自带随机盐, 单向不可逆);
 * 为兼容存量数据, 校验时自动识别历史 MD5 密文并放行, 待用户下次登录或修改密码后自动升级为 BCrypt 密文。
 * </p>
 * @date: 2026-10-20
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtils {

    /**
     * BCrypt 密文前缀 - BCrypt 密文以该前缀开头, 长度为 60
     */
    private static final String BCRYPT_PREFIX = "$2";

    /**
     * MD5 密文长度 - 32 位十六进制字符串
     */
    private static final int MD5_LENGTH = 32;

    /**
     * 十六进制字符集
     */
    private static final String HEX_CHARS = "0123456789abcdef";

    /**
     * 对明文密码进行加密, 统一使用 BCrypt 算法
     *
     * @param rawPassword 明文密码
     * @return BCrypt 密文
     */
    public static String encode(String rawPassword) {
        if (StrUtil.isBlank(rawPassword)) {
            return rawPassword;
        }
        return BCrypt.hashpw(rawPassword);
    }

    /**
     * 校验明文密码与密文是否匹配
     * <p>
     * 密文为 BCrypt 格式时使用 BCrypt 校验; 密文为历史 MD5 格式时使用 MD5 校验, 以保证存量数据可继续登录。
     * </p>
     *
     * @param rawPassword     明文密码
     * @param encodedPassword 数据库中存储的密文
     * @return 是否匹配
     */
    public static boolean matches(String rawPassword, String encodedPassword) {
        if (StrUtil.isBlank(rawPassword) || StrUtil.isBlank(encodedPassword)) {
            return false;
        }
        if (isBcrypt(encodedPassword)) {
            return BCrypt.checkpw(rawPassword, encodedPassword);
        }
        if (isLegacyMd5(encodedPassword)) {
            return MD5.create().digestHex(rawPassword).equalsIgnoreCase(encodedPassword);
        }
        return false;
    }

    /**
     * 判断密文是否为 BCrypt 格式
     *
     * @param encodedPassword 密文
     * @return 是否为 BCrypt 格式
     */
    public static boolean isBcrypt(String encodedPassword) {
        return StrUtil.isNotBlank(encodedPassword) && encodedPassword.startsWith(BCRYPT_PREFIX);
    }

    /**
     * 判断密文是否为历史遗留的 MD5 格式
     *
     * @param encodedPassword 密文
     * @return 是否为 MD5 格式
     */
    public static boolean isLegacyMd5(String encodedPassword) {
        if (StrUtil.isBlank(encodedPassword) || encodedPassword.length() != MD5_LENGTH) {
            return false;
        }
        for (char ch : encodedPassword.toCharArray()) {
            if (HEX_CHARS.indexOf(Character.toLowerCase(ch)) < 0) {
                return false;
            }
        }
        return true;
    }
}