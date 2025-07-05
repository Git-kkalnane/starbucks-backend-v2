package git_kkalnane.starbucksbackenv2.domain.order.dto.request;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

/**
 * 주문 생성 시, 개별 주문 상품의 정보를 담는 DTO
 *
 * @param itemId               주문할 상품의 고유 ID
 * @param itemType             상품의 타입
 * @param quantity             주문할 상품의 수량
 * @param shotQuantity         (음료) 추가된 샷의 수량
 * @param selectedSizes        (음료) 선택된 사이즈
 * @param selectedTemperatures (음료) 선택된 온도
 * @param options              (음료) 선택된 커스텀 옵션 목록
 */
public record OrderItemRequest(
        @NotNull Long itemId,
        @NotNull ItemType itemType,
        @NotNull @Positive Integer quantity,
        Integer shotQuantity,
        BeverageSizeOption selectedSizes,
        BeverageTemperatureOption selectedTemperatures,
        List<SelectedItemOptionRequest> options
) {
}