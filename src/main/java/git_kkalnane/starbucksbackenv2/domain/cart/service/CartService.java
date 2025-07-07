package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItemOption;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.ModifyCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.ModifyCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.CartQueryRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberErrorCode;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberException;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final MemberRepository memberRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CartItemOptionRepository cartItemOptionRepository;
    private final CartQueryRepository cartQueryRepository;

    @Transactional
    public CartItemResponse addCartItem(Long memberId, CartItemDto dto) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));


        List<CartItemOptionDto> optionDtos = dto.cartItemOptions() != null
                ? dto.cartItemOptions()
                : List.of();
        
        Long singleTotal = cartQueryRepository.calculateTotalPriceWithOption(dto.itemId(), optionDtos);


        Long beverageItemId = null, dessertItemId = null;
        if (dto.itemType() == ItemType.BEVERAGE) {
            beverageItemId = dto.itemId();
        } else if (dto.itemType() == ItemType.DESSERT) {
            dessertItemId = dto.itemId();
        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .itemType(dto.itemType())
                .quantity(dto.quantity())
                .beverageItemId(beverageItemId)
                .dessertItemId(dessertItemId)
                .imageUrl(dto.image())
                .selectedSizes(dto.cupSize())
                .selectedTemperatures(dto.temperatureOption())
                .itemPrice(singleTotal)
                .finalItemPrice(singleTotal * dto.quantity())
                .build();
        cartItem = cartItemRepository.save(cartItem);

        if (!optionDtos.isEmpty()) {
            List<CartItemOption> options = optionDtos.stream()
                    .map(option -> CartItemOption.builder()
                            .cartItemId(option.cartItemId())
                            .itemOptionId(option.itemOptionId())
                            .quantity(option.quantity())
                            .build())
                    .toList();
            cartItemOptionRepository.saveAll(options);
        }

        return CartItemResponse.of(cartItem, optionDtos);
    }

    @Transactional
    public ModifyCartItemResponse modifyCartItem(ModifyCartItemDto dto, Long memberId) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));
        CartItem cartItem = cartItemRepository.findById(dto.cartItemId())
                .orElseThrow(() -> new CartException(CartErrorCode.CART_ITEM_NOT_FOUND));

        cartItem.changeQuantity(dto.changeQuantity());

        Long finalPrice;
        if (cartItem.getItemType() == ItemType.DESSERT) {
            finalPrice = cartItem.getItemPrice() * dto.changeQuantity();
        } else {
            List<CartItemOption> savedOpts = cartItemOptionRepository.findAllByCartItemId(cartItem.getId());
            List<CartItemOptionDto> optDtos = savedOpts.stream()
                    .map(option -> new CartItemOptionDto(
                            option.getId(),
                            option.getCartItemId(),
                            option.getItemOptionId(),
                            option.getQuantity(),
                            null))
                    .toList();

            Long singleTotal = cartQueryRepository.calculateTotalPriceWithOption(
                    cartItem.getBeverageItemId(), optDtos);
            finalPrice = singleTotal * dto.changeQuantity();
        }
        cartItem.setFinalItemPrice(finalPrice);

        return new ModifyCartItemResponse(
                cartItem.getId(),
                cartItem.getItemType(),
                cartItem.getQuantity(),
                finalPrice
        );
    }

}
