package git_kkalnane.starbucksbackenv2.domain.cart.dto.response;

import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CheckCartItemDto;

import java.util.List;

public record CheckCartItemResponse(
        List<CheckCartItemDto> cartItemDto
) {
}
