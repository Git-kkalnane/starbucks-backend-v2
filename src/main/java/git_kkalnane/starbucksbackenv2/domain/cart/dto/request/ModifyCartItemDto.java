package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import jakarta.validation.constraints.NotNull;

public record ModifyCartItemDto(
        @NotNull Long cartItemId,
        int changeQuantity
) {
}
