package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import java.util.List;

public record OrderItemDto(
        Long itemId,
        String itemName,
        List<ItemOptionDto> options,
        Long finalPrice,
        Long quantity
) {
}
