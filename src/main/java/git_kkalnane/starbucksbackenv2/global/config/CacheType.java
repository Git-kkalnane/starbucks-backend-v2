package git_kkalnane.starbucksbackenv2.global.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 캐시별 개별 설정을 관리하는 Enum
 *
 * 각 캐시의 특성에 맞게 TTL(Time To Live)과 최대 크기를 개별 설정
 */
@Getter
@RequiredArgsConstructor
public enum CacheType {

    // ========== 개별 아이템 조회 캐시 ==========
    // 특성: 변경 빈도 낮음, 조회 빈도 높음 → 긴 TTL, 실제 데이터 기준 크기

    /**
     * 개별 음료 조회 캐시 - 실제 음료 수: 50개 - 여유분 포함: 70개로 설정<br>
     * <br>
     * 메뉴 조회 시 가장 자주 사용되는 핵심 캐시
     */
    BEVERAGE_ITEMS("beverageItems", 120, 70),

    /**
     * 개별 디저트 조회 캐시 - 실제 디저트 수: 30개 - 여유분 포함: 50개로 설정<br>
     * <br>
     * 음료 대비 조회 빈도는 낮으나 중요한 캐시
     */
    DESSERT_ITEMS("dessertItems", 120, 50),

    /**
     * 아이템별 옵션 조회 캐시 - 음료 50개 × 평균 10개 옵션 = 500개 - 여유분 포함: 600개로 설정<br>
     * <br>
     * 주문 시마다 조회되는 중요한 캐시
     */
    ITEM_OPTIONS("itemOptions", 180, 600),


    // ========== 목록 조회 캐시 ==========
    // 특성: 새 상품 추가 시 변경, 페이징별 다른 결과 → 중간 TTL, 페이징 기준 크기

    /**
     * 음료 목록 조회 캐시 (페이징) - 음료 50개 ÷ 10개 페이징 = 5페이지 - 다양한 정렬 옵션 고려 (이름순, 가격순, 인기순 등)<br>
     * <br>
     * 5페이지 × 5가지 정렬 = 25개 + 여유분 = 30개로 설정
     */
    BEVERAGE_LIST("beverageList", 60, 30),

    /**
     * 디저트 목록 조회 캐시 (페이징) - 디저트 30개 ÷ 10개 페이징 = 3페이지 - 다양한 정렬 옵션 고려<br>
     * <br>
     * 3페이지 × 5가지 정렬 = 15개 + 여유분 = 20개로 설정
     */
    DESSERT_LIST("dessertList", 60, 20),


    // ========== 복수 ID 조회 캐시 ==========
    // 특성: 주문 시 임시 조회, 조합이 다양함 → 짧은 TTL, 실제 주문 패턴 기준 크기

    /**
     * 복수 음료 ID 조회 캐시 - 한 주문당 평균 1-3개 음료<br>
     * <br>
     * 30분 TTL 동안 예상 주문 조합 수: 50-100개 - 피크 시간 고려하여 100개로 설정
     */
    BEVERAGES_BY_IDS("beveragesByIds", 30, 100),

    /**
     * 복수 디저트 ID 조회 캐시 - 한 주문당 평균 1-2개 디저트 - 음료보다 주문 빈도 낮음<br>
     * <br>
     * 30분 TTL 동안 예상 주문 조합 수: 30-50개 - 50개로 설정
     */
    DESSERTS_BY_IDS("dessertsByIds", 30, 50),

    /**
     * 복수 옵션 ID 조회 캐시 - 음료별 옵션 조합이 다양함 (샷, 시럽, 우유 등) - 옵션 조합의 다양성이 가장 높음<br>
     * <br>
     * 30분 TTL 동안 예상 조합 수: 100-200개 - 200개로 설정
     */
    ITEM_OPTIONS_BY_IDS("itemOptionsByIds", 30, 200);


    /**
     * 캐시 이름
     */
    private final String cacheName;
    /**
     * 캐시 만료 시간 (분 단위)
     */
    private final int expiredAfterWriteMinutes;
    /**
     * 최대 캐시 크기 (엔트리 개수)
     */
    private final int maximumSize;

    /**
     * 캐시 이름으로 CacheType을 찾는 메서드
     *
     * @param cacheName 찾을 캐시 이름
     * @return 해당하는 CacheType, 없으면 null
     */
    public static CacheType findByCacheName(String cacheName) {
        for (CacheType cacheType : values()) {
            if (cacheType.getCacheName().equals(cacheName)) {
                return cacheType;
            }
        }
        return null;
    }
}
