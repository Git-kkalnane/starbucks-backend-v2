package git_kkalnane.starbucksbackenv2.domain.cart;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItemOption;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.*;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CheckCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.ModifyCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartItemCreateService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartOptionService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.ValidAndCalculatorService;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {

    @InjectMocks
    private CartService cartService;


    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ValidAndCalculatorService validAndCalculatorService;
    @Mock
    private CartOptionService cartOptionService;
    @Mock
    private CartItemCreateService cartItemCreateService;
    @Mock
    private ItemOptionRepository itemOptionRepository;

    private Member member;
    private Cart cart;

    @BeforeEach
    void Test_세팅() {
        member = Member.builder().id(1L).build();
        cart = Cart.builder().id(1L).member(member).build();
    }

    @Test
    void addCartItemTest() {
        // given
        List<CartItemOptionDto> optionDtos = List.of(new CartItemOptionDto(1L, null, 10L, 1L, null));
        CartItemDto dto = new CartItemDto(
                null, 101L, "img.png", ItemType.BEVERAGE,
                BeverageTemperatureOption.HOT, optionDtos,
                BeverageSizeOption.TALL, 2, 7000L
        );

        CartItem cartItem = CartItem.builder()
                .id(10L)
                .cart(cart)
                .itemType(ItemType.BEVERAGE)
                .quantity(2)
                .build();

        when(validAndCalculatorService.findByMemberId(1L)).thenReturn(member);
        when(validAndCalculatorService.findCartByMemberId(1L)).thenReturn(cart);
        when(validAndCalculatorService.calculateSingleTotal(dto)).thenReturn(7000L);
        when(cartItemCreateService.createCartItem(cart, dto, 7000L)).thenReturn(cartItem);
        when(cartItemRepository.save(cartItem)).thenReturn(cartItem);

        // when
        CartItemResponse response = cartService.addCartItem(1L, dto);

        // then
        assertEquals(10L, response.id());
        verify(cartOptionService).saveCartItemOptions(cartItem, dto.cartItemOptions());
    }

    @Test
    void modifyCartItem() {
        // given
        ModifyCartItemDto dto = new ModifyCartItemDto(20L, 3);
        CartItem cartItem = CartItem.builder()
                .id(20L)
                .cart(cart)
                .itemType(ItemType.DESSERT)
                .itemPrice(4000L)
                .quantity(1)
                .build();

        when(validAndCalculatorService.findCartByMemberId(1L)).thenReturn(cart);
        when(validAndCalculatorService.findCartItemByCartId(20L)).thenReturn(cartItem);

        // when
        ModifyCartItemResponse response = cartService.modifyCartItem(dto, 1L);

        // then
        assertEquals(3, response.quantity());
        assertEquals(4000L * 3, response.finalPrice());
    }

    @Test
    void deleteCartItem() {
        // given
        DeleteCartItemDto dto = new DeleteCartItemDto(List.of(101L));
        CartItem cartItem = CartItem.builder()
                .id(101L)
                .cart(cart)
                .build();

        when(validAndCalculatorService.findCartByMemberId(1L)).thenReturn(cart);
        when(validAndCalculatorService.findCartItemByCartId(101L)).thenReturn(cartItem);

        // when
        List<Long> result = cartService.deleteCartItem(dto, 1L);

        // then
        assertEquals(1, result.size());
        assertTrue(result.contains(101L));
        verify(cartItemRepository).deleteById(101L);
    }

    @Test
    void getCartItem() {
        // given
        // 1. 먼저 Cart 생성
        Cart cart = Cart.builder().id(1L).build();

        // 2. 그다음 CartItem 생성 (옵션은 나중에 연결)
        CartItem cartItem = CartItem.builder()
                .id(11L)
                .cart(cart)
                .itemType(ItemType.DESSERT)
                .dessertItemId(222L)
                .quantity(2)
                .imageUrl("old-url")
                .build();

        // 3. CartItemOption 생성하면서 위에서 만든 cartItem 연결
        CartItemOption cartItemOption = CartItemOption.builder()
                .itemOptionId(111L)
                .quantity(2L)
                .cartItem(cartItem)
                .build();

        // 테스트용 CartItemOption을 cartItem에 강제로 붙이는 방법 (예: 필드에 직접 접근)
        ReflectionTestUtils.setField(cartItem, "cartItemOption", List.of(cartItemOption));


        DessertItem dessertItem = DessertItem.builder()
                .id(222L)
                .dessertItemNameKo("허니브레드")
                .imageUrl("dessert-img")
                .build();

        when(validAndCalculatorService.findCartByMemberId(1L)).thenReturn(cart);
        when(cartItemRepository.findAllByCartId(1L)).thenReturn(List.of(cartItem));
        when(validAndCalculatorService.findDessertItemByItemId(222L)).thenReturn(dessertItem);
        when(itemOptionRepository.findById(111L)).thenReturn(Optional.of(ItemOption.builder().id(111L).build()));

        // when
        CheckCartItemResponse response = cartService.getCartItem(1L);

        // then
        assertEquals(1, response.cartItemDto().size());
        CheckCartItemDto dto = response.cartItemDto().get(0);
        assertEquals("허니브레드", dto.itemName());
        assertEquals(2, dto.quantity());
    }




}