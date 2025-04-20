package com.barcommerce.barcommerce.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Ativa Cache e processamento assíncrono (@Async).
 */
@Configuration
@EnableCaching
@EnableAsync
public class CacheAsyncConfig {
    // O Spring Boot autoconfigura RedisConnectionFactory e RedisCacheManager
}