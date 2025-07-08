package git_kkalnane.starbucksbackenv2.domain.cart;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.CartQueryRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartItemCreateService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartOptionService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.ValidAndCalculatorService;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartSeviceTest2 {
    @InjectMocks
    private CartItemCreateService cartItemCreateService;
    @Mock
    private CartItemOptionRepository cartItemOptionRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private CartRepository cartRepository;
    @Mock private CartQueryRepository cartQueryRepository;
    @InjectMocks private ValidAndCalculatorService validAndCalculatorService;

    @InjectMocks
    private CartOptionService cartOptionService;


    @Test
    void createCartItem() {
        // given
        Cart cart = Cart.builder().id(1L).build();
        CartItemDto dto = new CartItemDto(
                null, 100L, "img.png", ItemType.BEVERAGE,
                BeverageTemperatureOption.HOT, List.of(), BeverageSizeOption.GRANDE, 2, 6000L
        );

        // when
        CartItem result = cartItemCreateService.createCartItem(cart, dto, 6000L);

        // then
        assertEquals(ItemType.BEVERAGE, result.getItemType());
        assertEquals(100L, result.getBeverageItemId());
        assertEquals(2, result.getQuantity());
        assertEquals(6000L * 2, result.getFinalItemPrice());
    }

    @Test
    void createCartItem_타입_일치() {
        // given
        Cart cart = Cart.builder().id(1L).build();
        CartItemDto dto = new CartItemDto(
                null, 101L, "img", null,
                null, List.of(), null, 1, 3000L
        );

        // expect
        assertThrows(CartException.class,
                () -> cartItemCreateService.createCartItem(cart, dto, 3000L));
    }

    @Test
    void saveCartItemOptions_옵션_저장() {
        // given
        CartItem cartItem = CartItem.builder().id(1L).build();
        List<CartItemOptionDto> optionDtos = List.of(
                new CartItemOptionDto(null, null, 10L, 2L, null),
                new CartItemOptionDto(null, null, 20L, 1L, null)
        );

        // when
        cartOptionService.saveCartItemOptions(cartItem, optionDtos);

        // then
        verify(cartItemOptionRepository).saveAll(anyList());
    }

    @Test
    void saveCartItemOptions_옵션_없이_저장() {
        // when
        cartOptionService.saveCartItemOptions(mock(CartItem.class), null);

        // then
        verify(cartItemOptionRepository, never()).saveAll(any());
    }

    @Test
    void saveCartItemOptions_빈_배열저장() {
        // when
        cartOptionService.saveCartItemOptions(mock(CartItem.class), List.of());

        // then
        verify(cartItemOptionRepository, never()).saveAll(any());
    }
    @Test
    void findByMemberId_잘저장되는지_확인() {
        // given
        Member member = Member.builder().id(1L).build();
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        Member result = validAndCalculatorService.findByMemberId(1L);

        // then
        assertEquals(1L, result.getId());
    }

    @Test
    void findCartByMemberId_없으면_에러() {
        // given
        when(cartRepository.findByMemberId(999L)).thenReturn(Optional.empty());


        assertThrows(CartException.class, () -> {
            validAndCalculatorService.findCartByMemberId(999L);
        });

    }

    @Test
    void calculateSingleTotal_음료_가격구하기테스트() {
        // given
        CartItemDto dto = new CartItemDto(
                null, 1L, null, ItemType.BEVERAGE, BeverageTemperatureOption.HOT,
                List.of(), BeverageSizeOption.TALL, 1, null
        );

        when(cartQueryRepository.calculateTotalPriceWithOption(eq(1L), anyList())).thenReturn(7000L);

        Long total = validAndCalculatorService.calculateSingleTotal(dto);
        assertEquals(7000L, total);
    }

    @Test
    void calculateSingleTotal_타입_검증() {
        // given
        CartItemDto dto = new CartItemDto(
                null, 999L, null, null, null, null, null, 1, null
        );

        // then
        assertThrows(CartException.class, () -> validAndCalculatorService.calculateSingleTotal(dto));
    }
}

