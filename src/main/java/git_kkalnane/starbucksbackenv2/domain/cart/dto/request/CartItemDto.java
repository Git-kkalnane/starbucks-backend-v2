package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartItemDto(
       Long id,
       Long itemId,
       String image,
       ItemType itemType,
       BeverageTemperatureOption temperatureOption ,
       @NotNull List<CartItemOptionDto> cartItemOptions,
       BeverageSizeOption cupSize,
       @Min(1) int quantity,
       Long priceWithOptions
) {
}
