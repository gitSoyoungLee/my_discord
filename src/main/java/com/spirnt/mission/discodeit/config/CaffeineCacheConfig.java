package com.spirnt.mission.discodeit.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
@Slf4j
class CaffeineCacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("users", "channels",
            "notifications");
        cacheManager.setCaffeine(caffeineCacheBuilder());
        return cacheManager;
    }

    @Bean
    public Caffeine<Object, Object> caffeineCacheBuilder() {
        return Caffeine.newBuilder()
            .expireAfterWrite(30, TimeUnit.MINUTES) // 쓰고 30분 후 만료
            .expireAfterAccess(10, TimeUnit.MINUTES)    // 무사용 10분 후 만료
            .maximumSize(5000)  // 최대 엔트리 수
            .recordStats()  // 통계 수집 활성화
            .removalListener((key, value, cause) -> // 제거 이벤트 리스너
                log.info("Cache removed: key={}, cause={}", key, cause));
    }

}
