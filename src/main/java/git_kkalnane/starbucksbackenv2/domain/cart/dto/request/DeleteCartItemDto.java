package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

import java.util.List;

public record DeleteCartItemDto(
        List<Long> cartItemId
) {
}
