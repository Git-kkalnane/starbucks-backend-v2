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
    private final ValidAndCalculatorService validAndCalculatorService;
    private final CartOptionService cartOptionService;
    private final CartItemCreateService cartItemCreateService;

    /**
     * 서비스 로직이 너무 길어져, 클래스를 따로 만들어 두어 간소화를 해보았습니다.
     * 자세한 설명은 클래스파일마다 주석 달아놓았습니다!
     */
    @Transactional
    public CartItemResponse addCartItem(Long memberId, CartItemDto cartItemDto) {
        Member member = validAndCalculatorService.findByMemberId(memberId);
        Cart cart = validAndCalculatorService.findCartByMemberId(memberId);

        Long singleTotal = validAndCalculatorService.calculateSingleTotal(cartItemDto);

        CartItem cartItem = cartItemCreateService.createCartItem(cart, cartItemDto, singleTotal);
        cartItem = cartItemRepository.save(cartItem);

        cartOptionService.saveCartItemOptions(cartItem.getId(), cartItemDto.cartItemOptions());

        return CartItemResponse.of(cartItem, cartItemDto.cartItemOptions());
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

    /**
     *
     * @param member : 카트가 존재하는 지 확인하기 위한 용도
     * @return : 만약 카트에 멤버가 없다면 카트 Repository에 저장
     */
    @Transactional
    public Cart createCartForMember(Member member) {
        // 이미 존재하는지 확인
        if (cartRepository.existsByMemberId(member.getId())) {
            throw new CartException(CartErrorCode.CART_ALREADY_EXISTS);
        }

        Cart cart = Cart.builder()
                .member(member)
                .build();
        return cartRepository.save(cart);
    }

}
