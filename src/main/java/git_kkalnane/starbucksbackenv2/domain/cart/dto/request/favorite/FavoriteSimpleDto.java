package git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;

public record FavoriteSimpleDto(

        Long itemId,
        String itemName,
        ItemType itemType,
        String itemDescription,
        String image,
        Long itemPrice,
        BeverageSizeOption beverageSizeOption,
        BeverageTemperatureOption beverageTemperatureOption

) {
}
