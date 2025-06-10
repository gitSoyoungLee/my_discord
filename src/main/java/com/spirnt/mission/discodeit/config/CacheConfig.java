package com.spirnt.mission.discodeit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@Slf4j
class CacheConfig {

    // Caffeine

//    @Bean
//    public CaffeineCacheManager cacheManager() {
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users", "channels",
//            "notifications");
//        cacheManager.setCaffeine(caffeineCacheBuilder());
//        return cacheManager;
//    }
//
//    @Bean
//    public Caffeine<Object, Object> caffeineCacheBuilder() {
//        return Caffeine.newBuilder()
//            .expireAfterWrite(30, TimeUnit.MINUTES) // 쓰고 30분 후 만료
//            .expireAfterAccess(10, TimeUnit.MINUTES)    // 무사용 10분 후 만료
//            .maximumSize(5000)  // 최대 엔트리 수
//            .recordStats()  // 통계 수집 활성화
//            .removalListener((key, value, cause) -> // 제거 이벤트 리스너
//                log.info("Cache removed: key={}, cause={}", key, cause));
//    }

    // Redis
    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return new GenericJackson2JsonRedisSerializer(mapper);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(
            "host.docker.internal",
            6379);
        config.setPassword("yourPassword");
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .serializeKeysWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new StringRedisSerializer())
            )
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    genericJackson2JsonRedisSerializer()
                )
            )
            .entryTtl(Duration.ofMinutes(30))        // TTL 30분
            .disableCachingNullValues();             // null 값 캐싱 금지
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        return RedisCacheManager.builder(factory) // RedisConnectionFactory로 캐시 매니저 구
            .cacheDefaults(cacheConfiguration()) // 위에서 세팅한 기본 캐시 설정 적용
            .transactionAware()                     // 트랜잭션 내 캐시 반영
            .build(); // RedisCacheManager 인스턴스를 생성하고 등록해 줌
    }
}
