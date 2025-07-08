package git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite;

public record FavoriteCartItemOptionDto(
        Long id,
        Long favoriteCartItemId,
        Long itemOptionId,
        Long quantity,
        String itemOptionName
) {
}
