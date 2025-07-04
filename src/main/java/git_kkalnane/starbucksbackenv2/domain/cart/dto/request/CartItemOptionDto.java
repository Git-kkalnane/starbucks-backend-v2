package git_kkalnane.starbucksbackenv2.domain.cart.dto.request;

public record CartItemOptionDto(
        Long id,
        Long cartItemId,
        Long itemOptionId,
        Long quantity,
        String itemOptionName
) {
}
