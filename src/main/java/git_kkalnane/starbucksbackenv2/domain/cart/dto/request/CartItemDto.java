package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import java.util.List;

public record CartItemDto(

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
