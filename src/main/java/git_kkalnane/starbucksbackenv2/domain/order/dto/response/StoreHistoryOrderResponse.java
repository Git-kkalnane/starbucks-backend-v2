package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItem;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [매장용] 과거 주문 내역에 표시될 단일 주문의 상세 정보를 담는 DTO.
 *
 * @param id                      주문 ID
 * @param orderNumber             주문 번호
 * @param orderTotalPrice         주문 총액
 * @param orderStatus             주문 상태 (COMPLETED 또는 CANCELLED)
 * @param pickupType              픽업 유형
 * @param orderRequestMemo        요청 사항
 * @param orderExpectedPickupTime 예상 픽업 시간
 * @param storeName               매장 이름
 * @param memberNickname          주문자 닉네임
 * @param orderItems              주문 상품 목록
 */
public record StoreHistoryOrderResponse(
        Long id,
        String orderNumber,
        Long orderTotalPrice,
        OrderStatus orderStatus,
        PickupType pickupType,
        String orderRequestMemo,
        LocalDateTime orderExpectedPickupTime,
        String storeName,
        String memberNickname,
        List<OrderItemSummary> orderItems
) {

    /**
     * 주문에 포함된 개별 상품의 요약 정보를 나타내는 내부 DTO.
     */
    private record OrderItemSummary(
            String itemName,
            Integer quantity
    ) {
        public static OrderItemSummary from(OrderItem orderItem) {
            return new OrderItemSummary(
                    orderItem.getItemName(),
                    orderItem.getQuantity()
            );
        }
    }

    public static StoreHistoryOrderResponse from(Order order) {
        List<OrderItemSummary> itemSummaries = order.getOrderItems().stream()
                .map(OrderItemSummary::from)
                .toList();

        return new StoreHistoryOrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getPickupType(),
                order.getRequestMemo(),
                order.getExpectedPickupTime(),
                order.getStore().getName(),
                order.getMember().getNickname(),
                itemSummaries
        );
    }
}