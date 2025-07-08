package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartItemQueryDto {
    private Long id;
    private Long itemId;
    private Enum itemType;
    private Long quantity;
    private Enum cupSize;
    private Enum temperatureOption;
}
