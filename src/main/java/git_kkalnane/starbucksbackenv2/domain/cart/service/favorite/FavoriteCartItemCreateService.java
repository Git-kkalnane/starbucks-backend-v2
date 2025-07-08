package git_kkalnane.starbucksbackenv2.domain.cart.service.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoriteCartItemCreateService {

    private final DessertItemRepository dessertItemRepository;
    private final BeverageItemRepository beverageItemRepository;

    public FavoriteCartItem createFavoriteCartItem(FavoriteCart favoriteCart, FavoriteSimpleDto dto, Long singleTotal) {
        Long beverageItemId = null, dessertItemId = null;
        String itemName="", description="";
        if (dto.itemType() == ItemType.BEVERAGE) {
            BeverageItem beverage = beverageItemRepository.findById(dto.itemId())
                    .orElseThrow(() -> new CartException(CartErrorCode.INVALID_ITEM));
            beverageItemId = beverage.getId();
            itemName = beverage.getItemNameKo();
            description = beverage.getDescription();
        } else if (dto.itemType() == ItemType.DESSERT) {
            DessertItem dessert = dessertItemRepository.findById(dto.itemId())
                    .orElseThrow(() -> new CartException(CartErrorCode.INVALID_ITEM));
            dessertItemId = dessert.getId();
            itemName = dessert.getDessertItemNameKo();
            description = dessert.getDescription();

        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }

        return FavoriteCartItem.builder()
                .favoriteCart(favoriteCart)
                .itemType(dto.itemType())
                .beverageItemId(beverageItemId)
                .dessertItemId(dessertItemId)
                .imageUrl(dto.image())
                .selectedSizes(dto.beverageSizeOption())
                .selectedTemperatures(dto.beverageTemperatureOption())
                .itemPrice(singleTotal)
                .itemName(itemName)
                .itemDescription(description)
                .build();
    }

    public FavoriteCartItem createFavoriteCartItems(FavoriteCart favoriteCart, FavoriteCartItemDto dto, Long singleTotal) {
        Long beverageItemId = null, dessertItemId = null;
        String itemName="", description="";
        if (dto.itemType() == ItemType.BEVERAGE) {
            BeverageItem beverage = beverageItemRepository.findById(dto.itemId())
                    .orElseThrow(() -> new CartException(CartErrorCode.INVALID_ITEM));
            beverageItemId = beverage.getId();
            itemName = beverage.getItemNameKo();
            description = beverage.getDescription();
        } else if (dto.itemType() == ItemType.DESSERT) {
            DessertItem dessert = dessertItemRepository.findById(dto.itemId())
                    .orElseThrow(() -> new CartException(CartErrorCode.INVALID_ITEM));
            dessertItemId = dessert.getId();
            itemName = dessert.getDessertItemNameKo();
            description = dessert.getDescription();

        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }
        return FavoriteCartItem.builder()
                .favoriteCart(favoriteCart)
                .itemType(dto.itemType())
                .quantity(dto.quantity())
                .beverageItemId(beverageItemId)
                .dessertItemId(dessertItemId)
                .imageUrl(dto.image())
                .selectedSizes(dto.cupSize())
                .selectedTemperatures(dto.temperatureOption())
                .itemPrice(singleTotal)
                .itemName(itemName)
                .itemDescription(description)
                .build();
    }
}



