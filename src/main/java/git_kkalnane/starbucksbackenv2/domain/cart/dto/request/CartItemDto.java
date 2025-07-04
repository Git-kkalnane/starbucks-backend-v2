package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartItemDto(

       Long id,
       Long itemId,
       String image,
       Enum itemType,
       Enum temperatureOption ,
       @NotNull List<CartItemOptionDto> cartItemOptions,
       Enum cupSize,
       @Min(1) Long quantity,
       Long priceWithOptions

) {
}
