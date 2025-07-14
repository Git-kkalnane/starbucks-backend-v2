package git_kkalnane.starbucksbackenv2.domain.cart;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartItemCreateService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartOptionService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartService;
import git_kkalnane.starbucksbackenv2.domain.cart.service.ValidAndCalculatorService;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
@DisplayName("장바구니 플로우 통합 테스트")
class CartTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired private CartRepository cartRepository;
    @Autowired private BeverageItemRepository beverageItemRepository;
    @Autowired private CartItemRepository cartItemRepository;
    @Autowired private ValidAndCalculatorService validAndCalculatorService;
    @Autowired private CartItemCreateService cartItemCreateService;
    @Autowired private CartOptionService cartOptionService;

    private Member member;
    private Cart cart;
    @Autowired
    private CartService cartService;

    // Github Actions 워크플로를 통과하지 못하는 테스트

    @BeforeEach
    void setup() {
        // 1. 회원가입 + 카트 자동 생성
        member = memberRepository.save(Member.builder()
                .name("테스트회원")
                .email("test@starbucks.com")
                .build());

        cartService.createCartForMember(member);

        cart = cartRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("Cart가 자동 생성되지 않았습니다."));
    }

    @Test
    @DisplayName("장바구니에 담기 → 조회 → 수량 수정 → 삭제")
    void cartItemFullFlow() {
        // 2. 메뉴 준비 (DB에 BeverageItem 등록)
        Long beverageId = 101L;
        BeverageItem beverage = beverageItemRepository.findById(beverageId)
                .orElseThrow(() -> new RuntimeException("해당 음료가 존재하지 않습니다."));
        // 3. 장바구니 DTO 생성
        CartItemDto cartItemDto = new CartItemDto(
                101L,
                beverage.getId(),
                "img.jpg",
                ItemType.BEVERAGE,
                BeverageTemperatureOption.ICE,
                List.of(),
                BeverageSizeOption.GRANDE,
                2,
                null
        );

        // 4. 가격 계산
        Long singlePrice = validAndCalculatorService.calculateSingleTotal(cartItemDto);

        // 5. 장바구니에 담기
        CartItem cartItem = cartItemCreateService.createCartItem(cart, cartItemDto, singlePrice);
        cartItem = cartItemRepository.save(cartItem);

        // 6. 옵션도 저장 (비어있는 경우)
        cartOptionService.saveCartItemOptions(cartItem, cartItemDto.cartItemOptions());

        // 검증 1: 장바구니에 저장되었는지
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());
        assertThat(cartItems).hasSize(1);
        assertThat(cartItems.get(0).getQuantity()).isEqualTo(2);

        // 검증 2: 수량 수정
        CartItem toModify = cartItems.get(0);
        toModify.changeQuantity(3);
        cartItemRepository.save(toModify);

        CartItem updated = cartItemRepository.findById(toModify.getId()).orElseThrow();
        assertThat(updated.getQuantity()).isEqualTo(3);

        // 검증 3: 삭제
        cartItemRepository.deleteById(updated.getId());

        List<CartItem> afterDelete = cartItemRepository.findAllByCartId(cart.getId());
        assertThat(afterDelete).isEmpty();
    }
}
