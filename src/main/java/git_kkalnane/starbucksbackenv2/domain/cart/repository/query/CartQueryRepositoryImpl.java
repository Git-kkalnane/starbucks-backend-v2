package git_kkalnane.starbucksbackenv2.domain.cart.repository.query;

import com.querydsl.jpa.impl.JPAQueryFactory;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.QCart;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.QItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.QBeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.QDessertItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Long calculateTotalPriceWithOption(Long itemId, List<CartItemOptionDto> optionDtos) {
      
        QBeverageItem beverageItem = QBeverageItem.beverageItem;
        QItemOption option = QItemOption.itemOption;

        Integer basePrice = queryFactory
                .select(beverageItem.price)
                .from(beverageItem)
                .where(beverageItem.id.eq(itemId))
                .fetchOne();

        if (basePrice == null) {
            throw new CartException(CartErrorCode.INVALID_ITEM);
        }

        long totalOptionPrice = 0;

        for (CartItemOptionDto itemOptionDto : optionDtos) {
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
}


