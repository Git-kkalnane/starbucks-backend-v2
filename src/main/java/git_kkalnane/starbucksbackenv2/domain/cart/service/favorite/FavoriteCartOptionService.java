package git_kkalnane.starbucksbackenv2.domain.cart.service.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItemOption;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartItemOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteCartOptionService {

    private final FavoriteCartItemOptionRepository favoriteCartItemOptionRepository;

    public void saveCartItemOptions(FavoriteCartItem favoriteCartItem, List<FavoriteCartItemOptionDto> optionDtos) {
        if (optionDtos == null || optionDtos.isEmpty()) return;

        List<FavoriteCartItemOption> options = optionDtos.stream()
                .map(dto -> FavoriteCartItemOption.builder()
                        .favoriteCartItem(favoriteCartItem)
                        .itemOptionId(dto.itemOptionId())
                        .quantity(dto.quantity())
                        .build())
                .toList();

        favoriteCartItemOptionRepository.saveAll(options);
    }
}
