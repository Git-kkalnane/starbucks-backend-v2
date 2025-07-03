package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;

import java.time.LocalDateTime;

/**
 * 사용자의 과거 주문 내역 목록에 표시될 단일 주문의 요약 정보를 담는 DTO.
 *
 * @param orderId          주문 ID
 * @param orderNumber      주문 번호
 * @param storeName        주문한 매장 이름
 * @param orderStatus      주문 상태 (COMPLETED 또는 CANCELED)
 * @param totalPrice       주문 총액
 * @param orderCompletedAt 주문 완료/취소 시간
 */
public record CustomerOrderHistoryResponse(
        Long orderId,
        String orderNumber,
        String storeName,
        OrderStatus orderStatus,
        Long totalPrice,
        LocalDateTime orderCompletedAt
) {
    public static CustomerOrderHistoryResponse from(Order order) {
        return new CustomerOrderHistoryResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStore().getName(),
                order.getStatus(),
                order.getTotalPrice(),
                order.getModifiedAt()
        );
    }
}