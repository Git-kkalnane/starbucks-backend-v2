package git_kkalnane.starbucksbackenv2.domain.order.dto.request;

import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * 주문 생성을 위한 요청 데이터를 담는 DTO.
 * 클라이언트가 보내는 가격 정보는 신뢰하지 않으므로, DTO에 가격 필드를 포함하지 않습니다.
 *
 * @param storeId      주문 매장 ID
 * @param pickupType   픽업 방식
 * @param requestMemo  고객 요청 사항
 * @param cardNumber   결제 카드 번호
 * @param orderItems   주문 상품 목록
 */
public record OrderCreateRequest(
        @NotNull Long storeId,
        @NotNull PickupType pickupType,
        String requestMemo,
        String cardNumber,
        @NotEmpty @Valid List<OrderItemRequest> orderItems
) {
}