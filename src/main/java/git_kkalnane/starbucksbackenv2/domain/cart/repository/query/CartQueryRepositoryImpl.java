package git_kkalnane.starbucksbackenv2.domain.cart.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.QCart;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.QItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.QBeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.QDessertItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("cartQueryRepository")
public class CartQueryRepositoryImpl implements CartQueryRepository {

    private final JPAQueryFactory queryFactory;

    public CartQueryRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Optional<Cart> findByMemberId(Long memberId) {
        QCart cart = QCart.cart;

        Cart result = queryFactory
                .selectFrom(cart)
                .where(cart.member.id.eq(memberId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Long calculateTotalPriceWithOption(Long itemId, List<Long> optionIds) {
        QBeverageItem beverageItem = QBeverageItem.beverageItem;
        QItemOption option = QItemOption.itemOption;
      
            Integer price = queryFactory
                    .select(beverageItem.price)
                    .from(beverageItem)
                    .where(beverageItem.id.eq(itemId))
                    .fetchOne();

            if (price == null) {
                throw new CartException(CartErrorCode.INVALID_ITEM);
            }

            Integer additionalPrice = queryFactory
                    .select(option.optionPrice.sum())
                    .from(option)
                    .where(option.id.in(optionIds))
                    .fetchOne();

            long safeAdditionalPrice = (additionalPrice != null) ? additionalPrice : 0L;


            return (price + safeAdditionalPrice);
        }

        @Override
    public Long calculatePrice(Long itemId) {
        QDessertItem dessertItem = QDessertItem.dessertItem;

        Integer price = queryFactory
                .select(dessertItem.price)
                .from(dessertItem)
                .where(dessertItem.id.eq(itemId))
                .fetchOne();

        return price == null ? 0L : price;
        }

        long safeAdditionalPrice = (additionalPrice != null) ? additionalPrice : 0L;

        return (price + safeAdditionalPrice);
    }

}

