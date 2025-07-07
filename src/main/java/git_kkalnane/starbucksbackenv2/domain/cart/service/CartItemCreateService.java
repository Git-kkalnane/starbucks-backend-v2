package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemCreateService {

    /**
     * Cart, CartItemDto, singleTotal(장바구니에는 1개의 메뉴씩 추가)를 통해 cartItem 생성
     * 하지만, Beverage인지, Dessert인지에 따라, singleTotal이 달라지기에 id만 검증 후 cartItem 생성
     */

    public CartItem createCartItem(Cart cart, CartItemDto dto, Long singleTotal) {
        Long beverageItemId = null, dessertItemId = null;
        if (dto.itemType() == ItemType.BEVERAGE) {
            beverageItemId = dto.itemId();
        } else if (dto.itemType() == ItemType.DESSERT) {
            dessertItemId = dto.itemId();
        } else {
            throw new CartException(CartErrorCode.INVALID_TYPE);
        }

        return CartItem.builder()
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
    }
}
