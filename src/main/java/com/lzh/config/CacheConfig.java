package com.lzh.config;

import com.lzh.cache.LayeringCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;


import java.time.Duration;

@Configuration
public class CacheConfig {

    /**
     * 配置缓存管理器
     *
     * @return 缓存管理器
     */

    /*    //@Primary
    @Bean("caffeineCacheManager")
    public CaffeineCacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(5, TimeUnit.SECONDS)
                // 初始的缓存空间大小
                .initialCapacity(100)
                // 缓存的最大条数
                .maximumSize(1000));
        return cacheManager;
    }*/

    @Autowired
    private RedisConnectionFactory connectionFactory;

    /*//@Primary
    @Bean("redisCacheManager") // 如果有多个CacheManager的话需要使用@Primary直接指定那个是默认的
    public RedisCacheManager redisCacheManager() {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);

        ObjectMapper om = new ObjectMapper();
        // 防止在序列化的过程中丢失对象的属性
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        // 开启实体类和json的类型转换
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);

        // 配置序列化（解决乱码的问题）
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig().serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)).serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                // 不缓存空值
                .disableCachingNullValues()
                // 1分钟过期
                .entryTtl(Duration.ofMinutes(1));
        RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
        return cacheManager;
    }*/


    /*@Bean(name = "compositeCacheManager")
    @Primary
    public CompositeCacheManager cacheManager(
            RedisCacheManager redisCacheManager,
            CaffeineCacheManager caffeineCacheManager) {

        CompositeCacheManager cacheManager = new CompositeCacheManager(

                redisCacheManager, caffeineCacheManager);

        return cacheManager;


    }*/

    @Bean
    @Primary
    public CacheManager cacheManager() {
        LayeringCacheManager layeringCacheManager = new LayeringCacheManager(RedisCacheWriter.lockingRedisCacheWriter(connectionFactory));

        return layeringCacheManager;
    }
}
