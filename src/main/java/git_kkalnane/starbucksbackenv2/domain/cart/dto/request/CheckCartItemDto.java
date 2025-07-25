package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import lombok.Builder;

import java.util.List;

@Builder
public record CheckCartItemDto(
        Long cartItemId,
        Long itemId,
        Long totalPrice,
        ItemType itemType,
        String itemName,
        int quantity,
        String imageUrl,
        BeverageSizeOption beverageSizeOption,
        BeverageTemperatureOption beverageTemperatureOption,
        List<CheckCartItemOptionDto> options
) {


}
