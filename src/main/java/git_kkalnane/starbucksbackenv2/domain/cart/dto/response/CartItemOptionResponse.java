package git_kkalnane.starbucksbackenv2.domain.cart.dto.response;

public record CartItemOptionResponse(
        Long id,
        Long cartItemId,
        Long itemOptionId,
        Long quantity,
        String itemOptionName
) {
}
