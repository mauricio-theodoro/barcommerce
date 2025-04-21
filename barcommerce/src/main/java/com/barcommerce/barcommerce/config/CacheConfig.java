// src/main/java/com/barcommerce/barcommerce/config/CacheConfig.java
package com.barcommerce.barcommerce.config;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Ativa e configura o uso de Redis como cache provider.
 * Usa JSON para serializar valores em cache — evita erros de proxy/JDK.
 */
@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        // 1) Define TTL de 5 minutos e desabilita caching de nulos
        RedisCacheConfiguration cfg = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(5))
                .disableCachingNullValues()
                // 2) Serializa todos os valores como JSON
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer())
                );

        // 3) Constrói o CacheManager para usar essa configuração
        return RedisCacheManager.builder(factory)
                .cacheDefaults(cfg)
                .build();
    }
}
