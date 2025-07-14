package git_kkalnane.starbucksbackenv2.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import git_kkalnane.starbucksbackenv2.global.utils.GlobalLogger;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 로컬 캐시 설정을 위한 구성 클래스<br>
 * <br>
 * Caffeine을 사용하여 고성능 로컬 캐시를 구성합니다. 상품 정보, 매장 정보 등 자주 조회되는 데이터를 캐싱하여 성능을 향상시킵니다.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * 캐시별 개별 설정이 적용된 CacheManager
     * <p>
     * 기존 방식: 모든 캐시가 동일한 설정 (30분 TTL, 1000개 크기) 새로운 방식: 캐시별 최적화된 개별 설정
     *
     * @return {@link CacheManager} 인스턴스
     */
    @Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(caffeineCaches());
        return cacheManager;
    }

    /**
     * CacheType Enum 기반으로 개별 설정된 캐시 리스트 생성
     *
     * @return 각각 다른 설정을 가진 {@link CaffeineCache} 리스트
     */
    public List<CaffeineCache> caffeineCaches() {
        return Arrays.stream(CacheType.values()).map(this::createCaffeineCacheForType).toList();
    }

    /**
     * 특정 CacheType에 맞는 CaffeineCache 생성
     *
     * @param cacheType 캐시 타입 설정
     * @return 개별 설정이 적용된 {@link CaffeineCache}
     */
    private CaffeineCache createCaffeineCacheForType(CacheType cacheType) {
        return new CaffeineCache(cacheType.getCacheName(), Caffeine.newBuilder()
            .maximumSize(cacheType.getMaximumSize())
            .expireAfterWrite(cacheType.getExpiredAfterWriteMinutes(), TimeUnit.MINUTES)
            .recordStats() // 캐시 통계 수집
            .removalListener((key, value, cause) -> {
                // 캐시별 제거 로깅
                GlobalLogger.info("Cache [{}] entry removed: {} due to {}",
                    cacheType.getCacheName(), key, cause);
            }).build());
    }
}
