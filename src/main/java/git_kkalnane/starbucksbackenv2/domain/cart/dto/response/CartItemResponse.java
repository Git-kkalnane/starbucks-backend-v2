package git_kkalnane.starbucksbackenv2.domain.cart.dto.response;

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
        Long quantity,
        Long priceWithOptions

) {
}
