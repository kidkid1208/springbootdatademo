package com.lzh.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * @author yuhao.wang
 */
public class LayeringCacheManager extends RedisCacheManager {
    @Autowired
    private RedisConnectionFactory connectionFactory;
    // 常量
    static final int DEFAULT_EXPIRE_AFTER_WRITE = 30;
    static final int DEFAULT_INITIAL_CAPACITY = 5;
    static final int DEFAULT_MAXIMUM_SIZE = 1_000;

    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<String, Cache>(16);

    private boolean dynamic = true;

    private boolean allowNullValues = true;

    // Caffeine 属性
    // 一级缓存默认有效时间60秒
    private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder()
            .expireAfterWrite(DEFAULT_EXPIRE_AFTER_WRITE, TimeUnit.SECONDS)
            .initialCapacity(DEFAULT_INITIAL_CAPACITY)
            .maximumSize(DEFAULT_MAXIMUM_SIZE);

    // redis 属性

    private static RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofSeconds(60L))
            .disableCachingNullValues();
    private  Map<String, RedisCacheConfiguration> initialCacheConfiguration;
    // reids key默认永远不过期时间

    public LayeringCacheManager(RedisCacheWriter cacheWriter) {
        this(cacheWriter, defaultCacheConfig, Collections.<String>emptyList(), false);
    }


    public LayeringCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, Collection<String> cacheNames) {
        this(cacheWriter, defaultCacheConfig, cacheNames, false);
    }

    public LayeringCacheManager(RedisCacheWriter cacheWriter, RedisCacheConfiguration defaultCacheConfig, Collection<String> cacheNames, boolean allowNullValues) {
        super(cacheWriter, defaultCacheConfig, true);
        this.allowNullValues = allowNullValues;

        setCacheNames(cacheNames);
    }


    protected RedisCache createRedisCache(String name) {
        return super.createRedisCache(name, defaultCacheConfig);
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = this.cacheMap.get(name);
        if (cache == null && this.dynamic) {
            synchronized (this.cacheMap) {
                cache = this.cacheMap.get(name);
                if (cache == null) {
                    cache = createCache(name);
                    this.cacheMap.put(name, cache);
                }
            }
        }
        return cache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }

    protected Cache createCache(String name) {
        return new LayeringCache(name, createRedisCache(name), createNativeCaffeineCache(name), isAllowNullValues());
    }

    /**
     * Create a native Caffeine Cache instance for the specified cache name.
     *
     * @param name the name of the cache
     * @return the native Caffeine Cache instance
     */
    protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
        return this.cacheBuilder.build();
    }

    /**
     * 使用该CacheManager的当前状态重新创建已知的缓存。
     */
    private void refreshKnownCaches() {
        for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
            entry.setValue(createCache(entry.getKey()));
        }
    }

    /**
     * 在初始化CacheManager的时候初始化一组缓存。
     * 使用这个方法会在CacheManager初始化的时候就会将一组缓存初始化好，并且在运行时不会再去创建更多的缓存。
     * 使用空的Collection或者重新在配置里面指定dynamic后，就可重新在运行时动态的来创建缓存。
     *
     * @param cacheNames
     */
    public void setCacheNames(Collection<String> cacheNames) {
        if (cacheNames != null) {
            for (String name : cacheNames) {
                this.cacheMap.put(name, createCache(name));
            }
            this.dynamic = cacheNames.isEmpty();
        }
    }


    /**
     * 获取是否允许Cache的值为null
     *
     * @return
     */
    public boolean isAllowNullValues() {
        return this.allowNullValues;
    }


}
