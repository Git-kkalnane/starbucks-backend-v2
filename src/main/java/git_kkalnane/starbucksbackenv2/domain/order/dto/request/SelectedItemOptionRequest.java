package git_kkalnane.starbucksbackenv2.domain.order.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * 주문 상품에 추가될 개별 옵션의 ID와 수량을 담는 DTO
 * @param itemOptionId 선택한 옵션의 ID
 * @param quantity 선택한 옵션의 수량
 */
public record SelectedItemOptionRequest(
        @NotNull Long itemOptionId,
        @NotNull @Positive int quantity
) {}