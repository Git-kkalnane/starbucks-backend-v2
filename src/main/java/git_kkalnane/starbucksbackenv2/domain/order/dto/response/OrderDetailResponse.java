package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * [공용] 주문의 모든 상세 정보를 담는 최종 응답 DTO (Record).
 * 고객용과 매장용 API에서 함께 사용됩니다.
 */
public record OrderDetailResponse(
        Long orderId,
        String orderNumber,
        OrderStatus orderStatus,
        LocalDateTime orderDateTime,
        PickupType pickupType,
        Long totalPrice,
        long totalItemCount,
        Long storeId,
        String storeName,
        Long memberId,
        String memberNickname,
        List<OrderItemDto> orderItems

) {
}
