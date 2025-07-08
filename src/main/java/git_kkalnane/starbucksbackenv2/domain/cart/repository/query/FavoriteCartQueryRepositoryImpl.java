package git_kkalnane.starbucksbackenv2.domain.cart.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.QFavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.domain.QItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.QBeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.QDessertItem;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("favoriteCartQueryRepository")
public class FavoriteCartQueryRepositoryImpl implements FavoriteCartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public FavoriteCartQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Optional<FavoriteCart> findByMemberId(Long memberId) {
        QFavoriteCart favoriteCart = QFavoriteCart.favoriteCart;

        FavoriteCart result = queryFactory
                .selectFrom(favoriteCart)
                .where(favoriteCart.member.id.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Long calculateBeveragePrice(Long itemId) {
        QBeverageItem beverageItem = QBeverageItem.beverageItem;

        Integer price = queryFactory
                .select(beverageItem.price)
                .from(beverageItem)
                .where(beverageItem.id.eq(itemId))
                .fetchOne();

        return price == null ? 0L : price;
    }

    @Override
    public Long calculateDessertPrice(Long itemId) {
        QDessertItem dessertItem = QDessertItem.dessertItem;

        Integer price = queryFactory
                .select(dessertItem.price)
                .from(dessertItem)
                .where(dessertItem.id.eq(itemId))
                .fetchOne();

        return price == null ? 0L : price;
    }

    @Override
    public Optional<BeverageItem> findBeverageItemById(Long id) {
        QBeverageItem beverageItem = QBeverageItem.beverageItem;

        BeverageItem result = queryFactory
                .selectFrom(beverageItem)
                .where(beverageItem.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<DessertItem> findDessertItemById(Long id) {
        QDessertItem dessertItem = QDessertItem.dessertItem;

        DessertItem result = queryFactory
                .selectFrom(dessertItem)
                .where(dessertItem.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Long priceCalculator(Long itemId) {
        QDessertItem dessertItem = QDessertItem.dessertItem;

        Integer price = queryFactory
                .select(dessertItem.price)
                .from(dessertItem)
                .where(dessertItem.id.eq(itemId))
                .fetchOne();

        return price == null ? 0L : price;
    }

    @Override
    public Long totalPriceWithOption(Long itemId, List<FavoriteCartItemOptionDto> optionDtos) {

        QBeverageItem beverageItem = QBeverageItem.beverageItem;
        QItemOption option = QItemOption.itemOption;


        Integer basePrice = queryFactory
                .select(beverageItem.price)
                .from(beverageItem)
                .where(beverageItem.id.eq(itemId))
                .fetchOne();
        System.out.println(basePrice);
        if (basePrice == null) {
            throw new CartException(CartErrorCode.INVALID_ITEM);
        }

        long totalOptionPrice = 0;


        for (FavoriteCartItemOptionDto itemOptionDto : optionDtos) {
            System.out.println(itemOptionDto.itemOptionId());
            Integer optionPrice = queryFactory
                    .select(option.optionPrice)
                    .from(option)
                    .where(option.id.eq(itemOptionDto.itemOptionId()))
                    .fetchOne();

            if (optionPrice == null) {
                throw new CartException(CartErrorCode.INVALID_ITEM);
            }

            totalOptionPrice += optionPrice * itemOptionDto.quantity(); // 수량 반영!
        }

        return basePrice + totalOptionPrice;
    }
}
