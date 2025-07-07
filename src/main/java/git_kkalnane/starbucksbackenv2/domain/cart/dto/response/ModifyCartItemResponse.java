package git_kkalnane.starbucksbackenv2.domain.cart.dto.response;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;

public record ModifyCartItemResponse(
        Long cartItemId,
        ItemType itemType,
        int quantity,
        Long finalPrice
) {
}
