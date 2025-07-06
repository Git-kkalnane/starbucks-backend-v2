package git_kkalnane.starbucksbackenv2.domain.order.dto.request;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;

/**
 * [매장용] 주문 상태 변경을 요청할 때 사용하는 DTO.
 *
 * @param newStatus 변경하고자 하는 새로운 주문 상태 (PREPARING, READY_FOR_PICKUP 등)
 */
public record StoreOrderStatusUpdateRequest(
        @NotNull(message = "변경할 주문 상태는 필수입니다.")
        OrderStatus newStatus
) {
}