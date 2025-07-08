package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;

/**
 * [고객용] 현재 주문 목록의 개별 주문 요약 정보를 담는 DTO.
 *
 * @param totalCount      해당 주문에 포함된 총 상품 수량
 * @param storeInfo       주문한 매장의 정보
 * @param orderNumber     고객에게 보여줄 주문 번호
 * @param orderStatus     현재 주문 상태
 * @param pickupType      픽업 타입
 * @param totalPrice      주문 총액
 */
public record CustomerCurrentOrderResponse(
        int totalCount,
        StoreInfo storeInfo,
        String orderNumber,
        OrderStatus orderStatus,
        PickupType pickupType,
        Long totalPrice
) {
    public static CustomerCurrentOrderResponse from(Order order) {
        int totalItemCount = order.getOrderItems().stream()
                .mapToInt(item -> item.getQuantity().intValue())
                .sum();

        return new CustomerCurrentOrderResponse(
                totalItemCount,
                StoreInfo.from(order.getStore()),
                order.getOrderNumber(),
                order.getStatus(),
                order.getPickupType(),
                order.getTotalPrice()
        );
    }

    /**
     * 주문에 포함될 매장 정보를 담는 내부 DTO.
     */
    public record StoreInfo(Long storeId, String storeName, String storeAddress) {
        public static StoreInfo from(Store store) {
            return new StoreInfo(store.getId(), store.getName(), store.getAddress());
        }
    }
}