package git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.CheckFavoriteCartItemDto;

import java.util.List;

public record CheckFavoriteCartItemResponse(
        List<CheckFavoriteCartItemDto> cartItemDto
) {
}