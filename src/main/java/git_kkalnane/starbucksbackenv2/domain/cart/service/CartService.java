package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItemOption;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.CartQueryRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberErrorCode;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberException;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType.BEVERAGE;
import static git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType.DESSERT;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartQueryRepository cartQueryRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final MemberRepository memberRepository;
    private final ItemOptionRepository itemOptionRepository;

    @Transactional
    public CartItemResponse addCartItem(Long memberId, CartItemDto cartItemDto) {

        Member member = memberRepository.findById(memberId).orElseThrow(
                ()-> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));

        List<Long> optionIds = cartItemDto.cartItemOptions() != null
                ? cartItemDto.cartItemOptions().stream().map(CartItemOptionDto::itemOptionId).toList()
                : List.of();

        Long totalPrice = 0L;

        if(cartItemDto.itemType() == BEVERAGE) {
            totalPrice = cartQueryRepository.calculateTotalPriceWithOption(cartItemDto.itemId(), optionIds);
        } else {
            totalPrice = cartQueryRepository.calculatePrice(cartItemDto.itemId());
        }


        Long beverageItemId = null, dessertItemId = null;

        switch (cartItemDto.itemType()) {
            case BEVERAGE:
                beverageItemId= cartItemDto.itemId();
                break;
            case DESSERT:
                dessertItemId = cartItemDto.itemId();
                break;
            default :
                throw new CartException(CartErrorCode.INVALID_TYPE);
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .itemType(cartItemDto.itemType())
                .quantity(cartItemDto.quantity())
                .beverageItemId(beverageItemId)
                .dessertItemId(dessertItemId)
                .imageUrl(cartItemDto.image())
                .selectedSizes(cartItemDto.cupSize())
                .selectedTemperatures(cartItemDto.temperatureOption())
                .itemPrice(totalPrice)
                .finalItemPrice(totalPrice)
                .build();
        cartItemRepository.save(cartItem);

        List<CartItemOptionDto> optionDtos = List.of();

        if(!optionIds.isEmpty()) {
            List<CartItemOption> options = cartItemDto.cartItemOptions().stream()
                    .map(cartItemOptionDto -> CartItemOption.builder()
                            .cartItemId(cartItem.getId())
                            .itemOptionId(cartItemOptionDto.itemOptionId())
                            .quantity(cartItemOptionDto.quantity())
                            .build())
                    .toList();
            cartItemOptionRepository.saveAll(options);

            optionDtos = options.stream()
                    .map(option -> new CartItemOptionDto(
                            option.getId(),
                            option.getCartItemId(),
                            option.getItemOptionId(),
                            option.getQuantity(),
                            itemOptionRepository.findNameById(option.getItemOptionId())
                    )).toList();
        }
        return new CartItemResponse(
                cartItem.getId(),
                cartItem.getBeverageItemId(),
                cartItem.getImageUrl(),
                cartItem.getItemType(),
                cartItem.getSelectedTemperatures(),
                optionDtos,
                cartItem.getSelectedSizes(),
                cartItem.getQuantity(),
                cartItemDto.priceWithOptions()
        );
    }

    @Transactional
    public Long deleteCartItem(Long cartItemId, Long memberId) {

        Cart cart = cartRepository.findByMemberId(memberId).orElseThrow(
                () -> new CartException(CartErrorCode.CART_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));

        if(!cartItem.getCart().getId().equals(cart.getId())) {
            throw new CartException(CartErrorCode.CART_INVALID);}

        cartItemRepository.deleteById(cartItemId);
        return cartItem.getId();
    }

}
