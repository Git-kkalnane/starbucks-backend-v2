package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

/**
 * 주문 생성 성공 시, 생성된 주문의 ID를 반환하는 DTO
 * @param orderId 새로 생성된 주문의 ID
 */
public record OrderCreateResponse(
        Long orderId
) {
}
