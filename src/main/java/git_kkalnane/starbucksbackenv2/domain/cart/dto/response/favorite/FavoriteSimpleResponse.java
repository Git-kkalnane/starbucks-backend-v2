package git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;


public record FavoriteSimpleResponse(
        Long itemId,
        String itemName,
        ItemType itemType,
        String itemDescription,
        String image,
        Long itemPrice,
        BeverageSizeOption beverageSizeOption,
        BeverageTemperatureOption beverageTemperatureOption
){
    public static FavoriteSimpleResponse of(FavoriteCartItem favoriteCartItem) {
        return new FavoriteSimpleResponse(
                favoriteCartItem.getId(),
                favoriteCartItem.getItemName(),
                favoriteCartItem.getItemType(),
                favoriteCartItem.getItemDescription(),
                favoriteCartItem.getImageUrl(),
                favoriteCartItem.getItemPrice(),
                favoriteCartItem.getSelectedSizes(),
                favoriteCartItem.getSelectedTemperatures()
        );
    }
}
