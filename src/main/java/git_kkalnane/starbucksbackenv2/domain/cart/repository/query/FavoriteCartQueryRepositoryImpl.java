package git_kkalnane.starbucksbackenv2.domain.cart.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.QFavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.QBeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.QDessertItem;
import org.springframework.stereotype.Repository;

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
}
