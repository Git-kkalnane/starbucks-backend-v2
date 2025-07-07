package git_kkalnane.starbucksbackenv2.domain.cart.dto.response;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;

import java.util.List;

public record CartItemResponse(
        Long id,
        Long itemId,
        String image,
        Enum itemType,
        Enum temperatureOption ,
        List<CartItemOptionDto> cartItemOptions,
        Enum cupSize,
        int quantity,
        Long priceWithOptions

) {
    public static CartItemResponse of(CartItem cartItem, List<CartItemOptionDto> optionDtos) {
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getBeverageItemId(),
                cartItem.getImageUrl(),
                cartItem.getItemType(),
                cartItem.getSelectedTemperatures(),
                optionDtos,
                cartItem.getSelectedSizes(),
                cartItem.getQuantity(),
                cartItem.getFinalItemPrice()
        );
    }
}
