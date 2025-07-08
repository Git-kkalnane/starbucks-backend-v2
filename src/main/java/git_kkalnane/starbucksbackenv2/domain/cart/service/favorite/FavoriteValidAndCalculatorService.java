package git_kkalnane.starbucksbackenv2.domain.cart.service.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.FavoriteCartQueryRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberErrorCode;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberException;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FavoriteValidAndCalculatorService {

    private final FavoriteCartRepository favoriteCartRepository;
    private final MemberRepository memberRepository;
    private final FavoriteCartQueryRepository favoriteCartQueryRepository;
    private final FavoriteCartItemRepository favoriteCartItemRepository;

    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    public FavoriteCart findFavoriteCartByMemberId(Long memberId) {
        return favoriteCartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));
    }

    public FavoriteCartItem findCartItemByCartId(Long cartItemId) {
        return favoriteCartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));
    }

    public void validateFavoriteSimpleDto(FavoriteSimpleDto dto) {
        if (dto.itemType() == ItemType.BEVERAGE) {
            BeverageItem beverage = favoriteCartQueryRepository.findBeverageItemById(dto.itemId())
                    .orElseThrow(() -> new CartException(CartErrorCode.ITEM_NOT_FOUND));

            if (!dto.itemName().equals(beverage.getItemNameKo()) ||
                    !dto.itemDescription().equals(beverage.getDescription())) {
                throw new CartException(CartErrorCode.INVALID_ITEM_INFO);
            }

        } else if (dto.itemType() == ItemType.DESSERT) {
            DessertItem dessert = favoriteCartQueryRepository.findDessertItemById(dto.itemId())
                    .orElseThrow(() -> new CartException(CartErrorCode.ITEM_NOT_FOUND));

            if (!dto.itemName().equals(dessert.getDessertItemNameKo()) ||
                    !dto.itemDescription().equals(dessert.getDescription())) {
                throw new CartException(CartErrorCode.INVALID_ITEM_INFO);
            }

        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }
    }



    public Long calculateSingleTotal(FavoriteSimpleDto favoriteSimpleDto) {
        if (favoriteSimpleDto.itemType() == ItemType.BEVERAGE) {
            return favoriteCartQueryRepository.calculateBeveragePrice(favoriteSimpleDto.itemId());
        } else if (favoriteSimpleDto.itemType() == ItemType.DESSERT) {
            return  favoriteCartQueryRepository.calculateDessertPrice(favoriteSimpleDto.itemId());
        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }
    }

    public Long calculateSingleTotalWithOption(FavoriteCartItemDto favoriteCartItemDto) {
        if (favoriteCartItemDto.itemType() == ItemType.BEVERAGE) {
            return favoriteCartQueryRepository.totalPriceWithOption(favoriteCartItemDto.itemId(),
                    Optional.ofNullable(favoriteCartItemDto.cartItemOptions()).orElse(List.of()));
        } else if (favoriteCartItemDto.itemType() == ItemType.DESSERT) {
            return favoriteCartQueryRepository.priceCalculator(favoriteCartItemDto.itemId());
        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }
    }
}
