package git_kkalnane.starbucksbackenv2.domain.cart.repository.query;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteCartQueryRepository {
    Optional<FavoriteCart> findByMemberId(Long memberId);
    Long calculateBeveragePrice(Long itemId);
    Long calculateDessertPrice(Long itemId);
    Optional<BeverageItem> findBeverageItemById(Long id);
    Optional<DessertItem> findDessertItemById(Long id);
    Long priceCalculator(Long itemId);
    Long totalPriceWithOption(Long itemId, List<FavoriteCartItemOptionDto> optionDtos);
}
