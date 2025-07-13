package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.CartQueryRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
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
public class ValidAndCalculatorService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartQueryRepository cartQueryRepository;
    private final BeverageItemRepository beverageItemRepository;
    private final DessertItemRepository dessertItemRepository;
    private final CartItemRepository cartItemRepository;

    /**
     * memberRepository에서 memberId를 찾고, 없으면 에러 발생
     */
    public Member findByMemberId(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * cartRepository에서 memberId를 찾고, 없으면 에러 발생
     */
    public Cart findCartByMemberId(Long memberId) {
        return cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));
    }

    public BeverageItem findBeverageItemByItemId(Long itemId) {
        return beverageItemRepository.findById(itemId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));
    }

    public DessertItem findDessertItemByItemId(Long itemId) {
        return dessertItemRepository.findById(itemId)
           .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));
    }

    public CartItem findCartItemByCartId(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
           .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));
    }

    /**
     * ItemType에 따라, 옵션 유무로 인해 가격이 달리 계산되기에, Type을 비교하여,
     * 미리 cartQueryRepository에 만들어 둔 로직으로 계산하여 singleTotal을 반환한다.
     * Beverage여도, option이 없을 수 있으니, 없을 시 빈 리스트 반환하도록 유도
     */
    public Long calculateSingleTotal(CartItemDto cartItemDto) {
        if (cartItemDto.itemType() == ItemType.BEVERAGE) {
            return cartQueryRepository.calculateTotalPriceWithOption(cartItemDto.itemId(),
                    Optional.ofNullable(cartItemDto.cartItemOptions()).orElse(List.of()));
        } else if (cartItemDto.itemType() == ItemType.DESSERT) {
            return cartQueryRepository.calculatePrice(cartItemDto.itemId());
        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }
    }



}
