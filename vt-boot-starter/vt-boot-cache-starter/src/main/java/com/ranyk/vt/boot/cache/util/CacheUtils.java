package com.ranyk.vt.boot.cache.util;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.benmanes.caffeine.cache.Cache;
import com.ranyk.vt.boot.base.constant.CacheTypeEnum;
import com.ranyk.vt.boot.base.exception.CacheException;
import com.ranyk.vt.boot.cache.config.properties.CacheConfigurationProperties;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * CLASS_NAME: CacheUtils.java
 *
 * @author ranyk
 * @version V1.0
 * @description: 缓存工具类
 * @date: 2025-09-26
 */
@Slf4j
@SuppressWarnings("unused")
public class CacheUtils {

    /**
     * Caffeine 缓存对象
     */
    private static volatile Cache<@NonNull String, Object> caffeineCache;
    /**
     * Redis 缓存对象
     */
    private static volatile RedisTemplate<@NonNull String, Object> redisTemplate;
    /**
     * 自定义缓存配置属性对象
     */
    private static volatile CacheConfigurationProperties cacheConfigurationProperties;
    /**
     * 常量定义 - 默认缓存类型
     */
    private static final CacheTypeEnum DEFAULT_CACHE_TYPE = CacheTypeEnum.CAFFEINE;
    /**
     * 批量缓存操作每个批次的大小
     */
    private static final int BATCH_SIZE = 1000;
    /**
     * 缓存值的默认值
     */
    private static final String DEFAULT_VALUE = "-";
    /**
     * 自定义的 ForkJoinPool 线程池，用于并行处理批量缓存操作
     */
    private static final ForkJoinPool CUSTOM_THREAD_POOL = new ForkJoinPool(10);

    /**
     * 获取缓存自动配置属性对象
     *
     * @return 缓存自动配置属性对象 {@link CacheConfigurationProperties}
     */
    private static CacheConfigurationProperties getCacheConfigurationProperties() {
        if (cacheConfigurationProperties == null) {
            synchronized (CacheUtils.class) {
                if (cacheConfigurationProperties == null) {
                    try {
                        cacheConfigurationProperties = SpringUtil.getBean(CacheConfigurationProperties.class);
                    } catch (Exception e) {
                        log.error("在获取缓存自动配置属性时发生异常, 异常信息 => ", e);
                        throw new CacheException("在获取缓存自动配置属性时发生异常", e);
                    }
                }
            }
        }
        return cacheConfigurationProperties;
    }

    /**
     * 获取 Caffeine 缓存对象
     *
     * @return Caffeine 缓存对象 {@link Cache}
     */
    private static Cache<@NonNull String, Object> getCaffeineCache() {
        if (caffeineCache == null) {
            synchronized (CacheUtils.class) {
                if (caffeineCache == null) {
                    CacheConfigurationProperties cacheConfigurationProperties = getCacheConfigurationProperties();
                    if (!cacheConfigurationProperties.getIsCaffeineEnabled()) {
                        log.error("未启用 Caffeine 缓存功能, 请先启用 Caffeine 缓存功能");
                        throw new CacheException("未启用 Caffeine 缓存功能, 请先启用 Caffeine 缓存功能");
                    }
                    try {
                        caffeineCache = SpringUtil.getBean("caffeineCache");
                    } catch (Exception e) {
                        if (e instanceof NoSuchBeanDefinitionException) {
                            log.error("未找到 Caffeine 缓存 bean, 请检查是否配置了 caffeineCache bean");
                            throw new CacheException("未找到 Caffeine 缓存 bean, 请检查是否配置了 caffeineCache bean");
                        }
                        log.error("获取 Caffeine 缓存对象失败, 发生异常 => ", e);
                        throw new CacheException("获取 Caffeine 缓存对象失败, 发生异常", e);
                    }
                }
            }
        }
        return caffeineCache;
    }

    /**
     * 获取 Redis 缓存对象
     *
     * @return Redis 缓存对象 {@link RedisTemplate}
     */
    private static RedisTemplate<@NonNull String, Object> getRedisTemplate() {
        if (redisTemplate == null) {
            synchronized (CacheUtils.class) {
                CacheConfigurationProperties cacheConfigurationProperties = getCacheConfigurationProperties();
                if (!cacheConfigurationProperties.getIsRedisEnabled()) {
                    log.error("未启用 Redis 缓存功能, 请先启用 Redis 缓存功能");
                    throw new CacheException("未启用 Redis 缓存功能, 请先启用 Redis 缓存功能");
                }
                if (redisTemplate == null) {
                    try {
                        redisTemplate = SpringUtil.getBean("redisTemplate");
                    } catch (Exception e) {
                        if (e instanceof NoSuchBeanDefinitionException) {
                            log.error("未找到 Redis 缓存 bean, 请检查是否配置了 redisTemplate bean");
                            throw new CacheException("未找到 Redis 缓存 bean, 请检查是否配置了 redisTemplate bean");
                        }
                        log.error("获取 Redis 缓存对象失败, 发生异常 => ", e);
                        throw new CacheException("获取 Redis 缓存对象失败, 发生异常", e);
                    }
                }
            }
        }
        return redisTemplate;
    }

    /**
     * 缓存数据 - 使用默认缓存方式(本地缓存)
     *
     * @param key   缓存的键
     * @param value 缓存的值
     */
    public static void cache(String key, Object value) {
        cache(key, value, DEFAULT_CACHE_TYPE);
    }

    /**
     * 缓存数据 - 使用 Redis 缓存方式
     *
     * @param key   缓存的键
     * @param value 缓存的值
     */
    public static void cacheWithRedis(String key, Object value) {
        cache(key, value, CacheTypeEnum.REDIS);
    }

    /**
     * 缓存数据 - 通过指定的缓存类型来进行缓存数据
     *
     * @param key   缓存的键
     * @param value 缓存的值
     * @param type  缓存类型, 参见 {@link CacheTypeEnum}
     */
    public static void cache(String key, Object value, CacheTypeEnum type) {
        // 参数校验
        validateKeyAndValue(key, value);
        // 默认使用本地缓存(类型1)，如果type为null或无效值
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            switch (cacheType) {
                case CAFFEINE -> {
                    getCaffeineCache().put(key, value);
                    log.trace("指定缓存类型后 -> 本地缓存存储成功 key: {}, value: {}", key, value);
                }
                case REDIS -> {
                    getRedisTemplate().opsForValue().set(key, value);
                    log.trace("指定缓存类型后 -> Redis缓存存储成功 key: {}, value: {}", key, value);
                }
                default -> {
                    log.warn("未知的缓存类型: {}, 使用默认本地缓存", cacheType);
                    caffeineCache.put(key, value);
                    log.trace("默认缓存 -> 本地缓存存储成功 key: {}, value: {}", key, value);
                }
            }
        } catch (Exception e) {
            if (e instanceof RedisConnectionFailureException){
                log.error("Redis 连接失败, 请检查 Redis 服务是否启动");
                throw new CacheException("Redis 缓存存储失败, Redis 连接失败, 请检查 Redis 服务是否启动", e);
            }
            if (e instanceof RedisConnectionException){
                log.error("Redis 缓存存储失败, Redis 连接异常, {}", e.getMessage());
                throw new CacheException(formatErrorMessage("Redis 缓存存储失败, Redis 连接异常, 异常信息为: " + e.getMessage()), e);
            }
            log.error("缓存存储失败 key: {}, value: {}, type: {}, 发生异常 => ", key, value, cacheType.name(), e);
            throw new CacheException(formatErrorMessage("缓存存储失败! key: %s, value: %s, type: %s", key, value, cacheType.name()), e);
        }
    }

    /**
     * 缓存数据并设置过期时间 - 使用默认缓存方式(本地缓存), 注意如果使用 Caffeine 缓存时, 过期时间参数将被忽略, 因为 Caffeine 缓存不支持运行时设置过期时间。
     *
     * @param key      缓存的键
     * @param value    缓存的值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public static void cache(String key, Object value, Long timeout, TimeUnit timeUnit) {
        cache(key, value, timeout, timeUnit, CacheTypeEnum.CAFFEINE);
    }

    /**
     * 缓存数据并设置过期时间 - 使用 Redis 缓存方式
     *
     * @param key      缓存的键
     * @param value    缓存的值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     */
    public static void cacheWithRedis(String key, Object value, Long timeout, TimeUnit timeUnit) {
        cache(key, value, timeout, timeUnit, CacheTypeEnum.REDIS);
    }

    /**
     * 缓存数据并设置过期时间 - 通过指定的缓存类型来进行缓存数据,注意如果使用 Caffeine 缓存时, 过期时间参数将被忽略, 因为 Caffeine 缓存不支持运行时设置过期时间。
     *
     * @param key      缓存的键
     * @param value    缓存的值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @param type     缓存类型, 参见 {@link CacheTypeEnum}
     */
    public static void cache(String key, Object value, Long timeout, TimeUnit timeUnit, CacheTypeEnum type) {
        // 参数校验
        validateKeyAndValue(key, value);
        // 参数校验 - 过期时间不存在时设置为 60
        timeout = Optional.ofNullable(timeout).orElse(60L);
        // 参数校验 - 时间单位不存在时设置为秒
        timeUnit = Optional.ofNullable(timeUnit).orElse(TimeUnit.SECONDS);
        // 参数校验 - 缓存类型不存在时设置为默认缓存类型(本地缓存)
        CacheTypeEnum cacheType = resolveCacheType(type);

        try {
            switch (cacheType) {
                case CAFFEINE -> {
                    getCaffeineCache().put(key, value);
                    log.trace("指定缓存类型后,由于 Caffeine缓存不支持运行时设置过期时间, 故只进行数据缓存 -> 本地缓存存储成功 key: {}, value: {}, timeout: {} {}", key, value, timeout, timeUnit);
                }
                case REDIS -> {
                    getRedisTemplate().opsForValue().set(key, value, timeout, timeUnit);
                    log.trace("指定缓存类型后,同时设置 Redis 缓存的过期时间和单位, Redis缓存存储成功 key: {}, value: {}, timeout: {} {}", key, value, timeout, timeUnit);
                }
                default -> {
                    log.warn("指定缓存类型后, 未知的缓存类型: {}, 使用默认本地缓存", cacheType.name());
                    getCaffeineCache().put(key, value);
                    log.trace("指定缓存类型后, 使用默认缓存 -> 本地缓存存储成功 key: {}, value: {}, timeout and timeUnit ignored", key, value);
                }
            }
        } catch (Exception e) {
            log.error("缓存数据失败 key: {}, value: {}, timeout: {} - {}, 发生异常 => ", key, value, timeout, timeUnit, e);
            throw new CacheException(formatErrorMessage("缓存数据失败！key: %s, value: %s, timeout: %d, timeUnit: %s", key, value, timeout, timeUnit), e);
        }
    }

    /**
     * 批量缓存数据 - 使用默认缓存方式(本地缓存)
     *
     * @param keyValueMap 键值对映射
     */
    public static void batchCache(Map<String, Object> keyValueMap) {
        batchCache(keyValueMap, DEFAULT_CACHE_TYPE);
    }

    /**
     * 批量缓存数据 - 使用 Redis 缓存方式
     *
     * @param keyValueMap 键值对映射
     */
    public static void batchCacheWithRedis(Map<String, Object> keyValueMap) {
        batchCache(keyValueMap, CacheTypeEnum.REDIS);
    }

    /**
     * 批量缓存数据 - 通过指定的缓存类型来进行缓存数据
     *
     * @param keyValueMap 键值对映射
     * @param type        缓存类型, 参见 {@link CacheTypeEnum}
     */
    public static void batchCache(Map<String, Object> keyValueMap, CacheTypeEnum type) {
        // 参数校验 - 需要进行缓存的数据不能不存在
        if (CollectionUtil.isEmpty(keyValueMap)) {
            throw new CacheException("批量缓存数据不能为空");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            switch (cacheType) {
                case CAFFEINE -> {
                    keyValueMap.forEach(getCaffeineCache()::put);
                    log.trace("批量缓存 - 指定缓存类型后,使用本地缓存 -> 本地缓存存储成功 count: {}", keyValueMap.size());
                }
                case REDIS -> {
                    getRedisTemplate().opsForValue().multiSet(keyValueMap);
                    log.trace("批量缓存 - 指定缓存类型后,使用 Redis 缓存 -> Redis缓存存储成功 count: {}", keyValueMap.size());
                }
                default -> {
                    log.warn("批量缓存 - 未指定缓存类型, 使用默认本地缓存: {}", cacheType.name());
                    keyValueMap.forEach(getCaffeineCache()::put);
                    log.trace("批量缓存 - 指定缓存类型后,使用默认缓存 -> 本地缓存存储成功 count: {}", keyValueMap.size());
                }
            }
        } catch (Exception e) {
            log.error("批量缓存数据失败 count: {} , 发生异常 => ", keyValueMap.size(), e);
            throw new CacheException(formatErrorMessage("批量缓存数据失败！count: %d", keyValueMap.size()), e);
        }
    }

    /**
     * 检查缓存键是否存在 - 使用默认缓存方式(本地缓存)
     *
     * @param key 缓存的键
     * @return 存在结果
     */
    public static Boolean exists(String key) {
        return exists(key, DEFAULT_CACHE_TYPE);
    }

    /**
     * 检查缓存键是否存在 - 使用 Redis 缓存方式
     *
     * @param key 缓存的键
     * @return 存在结果
     */
    public static Boolean existsWithRedis(String key) {
        return exists(key, CacheTypeEnum.REDIS);
    }

    /**
     * 检查缓存键是否存在 - 通过指定的缓存类型来进行缓存数据的存在性检查
     *
     * @param key  缓存的键
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 存在结果
     */
    public static Boolean exists(String key, CacheTypeEnum type) {
        // 参数校验 - 需要判断的缓存 key 不能为空
        if (StrUtil.isBlank(key)) {
            throw new CacheException("检查缓存是否存在的键不能为空!");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            return switch (cacheType) {
                case CAFFEINE -> getCaffeineCache().getIfPresent(key) != null;
                case REDIS -> getRedisTemplate().hasKey(key);
            };
        } catch (Exception e) {
            log.error("检查缓存键是否存在失败 key: {}", key, e);
            throw new CacheException(formatErrorMessage("检查缓存键是否存在失败！key: %s", key), e);
        }
    }

    /**
     * 批量检查缓存键是否存在 - 使用默认缓存方式(本地缓存)
     *
     * @param keys 缓存键集合
     * @return 存在的键集合
     */
    public static Set<String> batchExists(List<String> keys) {
        return batchExists(keys, DEFAULT_CACHE_TYPE);
    }

    /**
     * 批量检查缓存键是否存在 - 使用 Redis 缓存方式
     *
     * @param keys 缓存键集合
     * @return 存在的键集合
     */
    public static Set<String> batchExistsWithRedis(List<String> keys) {
        return batchExists(keys, CacheTypeEnum.REDIS);
    }

    /**
     * 批量检查缓存键是否存在 - 通过指定的缓存类型来进行缓存数据的存在性检查
     *
     * @param keys 缓存键集合
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 存在的键集合
     */
    public static Set<String> batchExists(List<String> keys, CacheTypeEnum type) {
        // 参数校验 - 需要进行缓存是否存在的
        if (CollectionUtil.isEmpty(keys)) {
            throw new CacheException("批量检查缓存键是否存在不能为空!");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            return switch (cacheType) {
                case CAFFEINE -> CUSTOM_THREAD_POOL.submit(() -> keys.stream()
                                .collect(Collectors.groupingBy(key -> keys.indexOf(key) / BATCH_SIZE))
                                .values()
                                .parallelStream()
                                .flatMap(Collection::stream)
                                .filter(key -> getCaffeineCache().getIfPresent(key) != null)
                                .collect(Collectors.toSet()))
                        .join();
                case REDIS -> CUSTOM_THREAD_POOL.submit(() -> keys.stream()
                                .collect(Collectors.groupingBy(key -> keys.indexOf(key) / BATCH_SIZE))
                                .values()
                                .parallelStream()
                                .flatMap(Collection::stream)
                                .filter(getRedisTemplate()::hasKey)
                                .collect(java.util.stream.Collectors.toSet()))
                        .join();
            };
        } catch (Exception e) {
            log.error("批量检查缓存键是否存在失败", e);
            throw new CacheException("批量检查缓存键是否存在失败！", e);
        }
    }

    /**
     * 获取缓存数据 - 使用默认缓存方式(本地缓存)
     *
     * @param key 缓存的键
     * @return 返回缓存的值
     */
    public static Object getCache(String key) {
        return getCache(key, DEFAULT_CACHE_TYPE);
    }

    /**
     * 获取缓存数据 - 使用 Redis 缓存方式
     *
     * @param key 缓存的键
     * @return 缓存的值
     */
    public static Object getCacheWithRedis(String key) {
        return getCache(key, CacheTypeEnum.REDIS);
    }

    /**
     * 获取缓存数据 - 根据指定的缓存类型来获取缓存数据
     *
     * @param key  缓存的键
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 返回缓存的值
     */
    public static Object getCache(String key, CacheTypeEnum type) {
        // 参数校验 - 缓存键不能为空
        if (StrUtil.isBlank(key)) {
            throw new CacheException("获取缓存数据的键不能为空!");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            return switch (cacheType) {
                case CAFFEINE -> Optional.ofNullable(getCaffeineCache().getIfPresent(key)).orElse(DEFAULT_VALUE);
                case REDIS -> Optional.ofNullable(getRedisTemplate().opsForValue().get(key)).orElse(DEFAULT_VALUE);
            };
        } catch (Exception e) {
            if (e instanceof CacheException) {
                throw (CacheException) e;
            }
            log.error("获取缓存数据失败 key: {}", key, e);
            throw new CacheException(formatErrorMessage("获取缓存数据失败！key: %s", key), e);
        }
    }

    /**
     * 批量获取缓存数据 - 使用默认缓存方式(本地缓存)
     *
     * @param keys 缓存键集合
     * @return 键值对映射
     */
    public static Map<String, Object> batchGetCache(List<String> keys) {
        return batchGetCache(keys, DEFAULT_CACHE_TYPE);
    }

    /**
     * 批量获取缓存数据 - 使用 Redis 缓存方式
     *
     * @param keys 缓存键集合
     * @return 键值对映射
     */
    public static Map<String, Object> batchGetCacheWithRedis(List<String> keys) {
        return batchGetCache(keys, CacheTypeEnum.REDIS);
    }

    /**
     * 批量获取缓存数据 - 根据指定的缓存类型来获取缓存数据
     *
     * @param keys 缓存键集合
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 键值对映射
     */
    public static Map<String, Object> batchGetCache(List<String> keys, CacheTypeEnum type) {
        // 参数校验 - 缓存键不能为空
        if (CollectionUtil.isEmpty(keys)) {
            throw new CacheException("批量获取缓存数据的键不能为空!");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            return switch (cacheType) {
                case CAFFEINE -> CUSTOM_THREAD_POOL.submit(() -> keys.stream()
                                .collect(Collectors.groupingBy(key -> keys.indexOf(key) / BATCH_SIZE))
                                .values()
                                .parallelStream()
                                .flatMap(Collection::stream)
                                .filter(key -> getCaffeineCache().getIfPresent(key) != null)
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        key -> Optional.ofNullable(getCaffeineCache().getIfPresent(key)).orElse(DEFAULT_VALUE),
                                        (existing, replacement) -> existing,
                                        LinkedHashMap::new
                                )))
                        .join();
                case REDIS -> CUSTOM_THREAD_POOL.submit(() -> keys.stream()
                                .collect(Collectors.groupingBy(key -> keys.indexOf(key) / BATCH_SIZE))
                                .values()
                                .parallelStream()
                                .flatMap(Collection::stream)
                                .filter(getRedisTemplate()::hasKey)
                                .collect(Collectors.toMap(
                                        Function.identity(),
                                        key -> Optional.ofNullable(getRedisTemplate().opsForValue().get(key)).orElse(DEFAULT_VALUE),
                                        (existing, replacement) -> existing,
                                        LinkedHashMap::new
                                )))
                        .join();
            };
        } catch (Exception e) {
            log.error("批量获取缓存数据失败", e);
            throw new CacheException("批量获取缓存数据失败！", e);
        }
    }

    /**
     * 删除缓存数据 - 使用默认缓存方式(本地缓存)
     *
     * @param key 缓存的键
     * @return 删除结果
     */
    public static Boolean deleteCache(String key) {
        return deleteCache(key, DEFAULT_CACHE_TYPE);
    }

    /**
     * 删除缓存数据 - 使用 Redis 缓存方式
     *
     * @param key 缓存的键
     * @return 删除结果
     */
    public static Boolean deleteCacheWithRedis(String key) {
        return deleteCache(key, CacheTypeEnum.REDIS);
    }

    /**
     * 批量删除缓存数据 - 根据指定的缓存类型来删除缓存数据
     *
     * @param key  缓存的键
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 删除结果
     */
    public static Boolean deleteCache(String key, CacheTypeEnum type) {
        // 参数校验 - 缓存键不能为空
        if (StrUtil.isBlank(key)) {
            throw new CacheException("删除缓存数据的键不能为空!");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            return switch (cacheType) {
                case CAFFEINE -> {
                    getCaffeineCache().invalidate(key);
                    // Caffeine 的 invalidate 操作总是成功的
                    yield Boolean.TRUE;
                }
                case REDIS -> getRedisTemplate().delete(key);
            };
        } catch (Exception e) {
            log.error("删除缓存数据失败 key: {}", key, e);
            throw new CacheException(formatErrorMessage("删除缓存数据失败！key: %s", key), e);
        }
    }

    /**
     * 批量删除缓存数据 - 使用默认缓存方式(本地缓存)
     *
     * @param keys 缓存键集合
     * @return 成功删除的数量
     */
    public static Long batchDeleteCache(List<String> keys) {
        return batchDeleteCache(keys, DEFAULT_CACHE_TYPE);
    }

    /**
     * 批量删除缓存数据 - 使用 Redis 缓存方式
     *
     * @param keys 缓存的键
     * @return 删除结果
     */
    public static Long batchDeleteCacheWithRedis(List<String> keys) {
        return batchDeleteCache(keys, CacheTypeEnum.REDIS);
    }

    /**
     * 批量删除缓存数据 - 根据指定的缓存类型来删除缓存数据
     *
     * @param keys 缓存的键
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 删除结果
     */
    public static Long batchDeleteCache(List<String> keys, CacheTypeEnum type) {
        // 参数校验 - 缓存键不能为空
        if (CollectionUtil.isEmpty(keys)) {
            throw new CacheException("批量删除缓存数据的键不能为空!");
        }
        CacheTypeEnum cacheType = resolveCacheType(type);
        try {
            return switch (cacheType) {
                case CAFFEINE -> {
                    getCaffeineCache().invalidateAll(keys);
                    // Caffeine的invalidateAll操作总是成功的
                    yield Long.parseLong(String.valueOf(keys.size()));
                }
                case REDIS -> getRedisTemplate().delete(keys);
            };
        } catch (Exception e) {
            log.error("批量删除缓存数据失败", e);
            throw new CacheException("批量删除缓存数据失败！", e);
        }
    }

    /**
     * 清空所有缓存 - 使用默认缓存方式(本地缓存)
     */
    public static void clearAllCache() {
        clearAllCache(DEFAULT_CACHE_TYPE);
    }

    /**
     * 清空所有缓存 - 使用 Redis 缓存方式
     */
    public static void clearAllCacheWithRedis() {
        clearAllCache(CacheTypeEnum.REDIS);
    }

    /**
     * 清空所有缓存 - 根据指定的缓存类型来清空缓存
     *
     * @param cacheType 缓存类型, 参见 {@link CacheTypeEnum}
     */
    public static void clearAllCache(CacheTypeEnum cacheType) {
        switch (cacheType) {
            case CAFFEINE -> getCaffeineCache().invalidateAll();
            case REDIS -> getRedisTemplate().execute((RedisCallback<Void>) connection -> {
                try {
                    connection.serverCommands().flushAll();
                    log.trace("清空所有缓存 - Redis 缓存已成功清空");
                } catch (Exception e) {
                    log.error("清空所有缓存 - Redis 缓存清空失败", e);
                    throw new CacheException("清空所有缓存 - Redis 缓存清空失败！", e);
                }
                return null;
            });
            default -> {
                log.trace("清空所有缓存 - 未指定缓存类型, 使用默认本地缓存: {}", cacheType.name());
                getCaffeineCache().invalidateAll();
            }
        }
    }

    /**
     * 获取缓存统计信息（仅适用于Caffeine）
     *
     * @return 统计信息字符串
     */
    public static String getCacheStats() {
        return getCacheStats(DEFAULT_CACHE_TYPE);
    }

    /**
     * 获取缓存统计信息（仅适用于Caffeine）
     *
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 统计信息字符串
     */
    public static String getCacheStats(CacheTypeEnum type) {
        CacheTypeEnum cacheType = resolveCacheType(type);
        return switch (cacheType) {
            case CAFFEINE -> getCaffeineCache().stats().toString();
            case REDIS -> "Redis缓存暂不支持统计信息";
        };
    }

    /**
     * 缓存参数校验
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    private static void validateKeyAndValue(String key, Object value) {
        if (StrUtil.isBlank(key)) {
            throw new CacheException("缓存键不能为空!");
        }
        if (Objects.isNull(value)) {
            throw new CacheException("缓存值不能为空!");
        }
    }

    /**
     * 根据指定的缓存类型来获取缓存对象 - 如果未指定缓存类型, 则使用默认缓存 {@link CacheTypeEnum#CAFFEINE}
     *
     * @param type 缓存类型, 参见 {@link CacheTypeEnum}
     * @return 缓存对象 {@link CacheTypeEnum}
     */
    private static CacheTypeEnum resolveCacheType(CacheTypeEnum type) {
        return switch (type) {
            case CAFFEINE, REDIS -> type;
            case null -> DEFAULT_CACHE_TYPE;
        };
    }

    /**
     * 格式化错误信息
     *
     * @param message 错误信息
     * @param args    参数
     * @return 格式化后的错误信息
     */
    private static String formatErrorMessage(String message, Object... args) {
        try {
            return String.format(message, args);
        } catch (Exception e) {
            throw new CacheException("缓存工具中格式化错误信息处发生异常", e);
        }
    }
}
