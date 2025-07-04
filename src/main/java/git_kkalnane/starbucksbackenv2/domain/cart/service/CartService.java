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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartQueryRepository cartQueryRepository;
    private final CartItemOptionRepository cartItemOptionRepository;

    @Transactional
    public CartItemResponse addCartItem(Long memberId, CartItemDto cartItemDto) {
        Cart cart = cartRepository.findByMemberId(memberId)
                .orElseThrow(() -> new CartException(CartErrorCode.CART_NOT_FOUND));

        List<Long> optionIds = cartItemDto.cartItemOptions() != null
                ? cartItemDto.cartItemOptions().stream().map(CartItemOptionDto::itemOptionId).toList()
                : List.of();

        Long totalPrice = cartQueryRepository.calculateTotalPriceWithOption(cartItemDto.itemId(), optionIds);

        CartItem cartItem = CartItem.builder()
                .cart(cart)
                .itemType((ItemType) cartItemDto.itemType())
                .quantity(cartItemDto.quantity())
                .beverageItemId(cartItemDto.itemId())
                .imageUrl(cartItemDto.image())
                .selectedSizes((BeverageSizeOption) cartItemDto.cupSize())
                .selectedTemperatures((BeverageTemperatureOption) cartItemDto.temperatureOption())
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
                            null
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

}
