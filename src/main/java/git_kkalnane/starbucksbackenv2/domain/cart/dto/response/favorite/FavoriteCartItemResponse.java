package git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;

import java.util.List;

public record FavoriteCartItemResponse(
        Long id,
        Long itemId,
        String image,
        ItemType itemType,
        BeverageTemperatureOption temperatureOption ,
        List<FavoriteCartItemOptionDto> cartItemOptions,
        BeverageSizeOption cupSize,
        int quantity
) {
    public static FavoriteCartItemResponse of(FavoriteCartItem cartItem, List<FavoriteCartItemOptionDto> optionDtos) {
        return new FavoriteCartItemResponse(
                cartItem.getId(),
                cartItem.getBeverageItemId(),
                cartItem.getImageUrl(),
                cartItem.getItemType(),
                cartItem.getSelectedTemperatures(),
                optionDtos,
                cartItem.getSelectedSizes(),
                cartItem.getQuantity()
        );
    }
}
