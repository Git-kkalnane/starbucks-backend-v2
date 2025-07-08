package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItemOption;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.*;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.DeleteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.ModifyCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.CheckCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.ModifyCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.CartQueryRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberErrorCode;
import git_kkalnane.starbucksbackenv2.domain.member.common.exception.MemberException;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private final ItemOptionRepository itemOptionRepository;

    /**
     * 서비스 로직이 너무 길어져, 클래스를 따로 만들어 두어 간소화를 해보았습니다.
     * 자세한 설명은 클래스파일마다 주석 달아놓았습니다!
     */
    @Transactional
    public CartItemResponse addCartItem(Long memberId, CartItemDto cartItemDto) {
        Member member = validAndCalculatorService.findByMemberId(memberId); //TODO : Member 사용 X 후에 로직 추가
        Cart cart = validAndCalculatorService.findCartByMemberId(memberId);

        Long singleTotal = validAndCalculatorService.calculateSingleTotal(cartItemDto);

        CartItem cartItem = cartItemCreateService.createCartItem(cart, cartItemDto, singleTotal);
        cartItem = cartItemRepository.save(cartItem);

        cartOptionService.saveCartItemOptions(cartItem, cartItemDto.cartItemOptions());

        return CartItemResponse.of(cartItem, cartItemDto.cartItemOptions());
    }


    @Transactional
    public ModifyCartItemResponse modifyCartItem(ModifyCartItemDto modifyCartItemDto, Long memberId) {

        Cart cart = validAndCalculatorService.findCartByMemberId(memberId); //TODO : Cart 검증 로직 후에 추가하겠습니다.
        CartItem cartItem = validAndCalculatorService.findCartItemByCartId(modifyCartItemDto.cartItemId());
      
        cartItem.changeQuantity(modifyCartItemDto.changeQuantity());

        Long finalPrice;
        if (cartItem.getItemType() == ItemType.DESSERT) {
            finalPrice = cartItem.getItemPrice() * modifyCartItemDto.changeQuantity();
        } else {
            List<CartItemOption> savedOpts = cartItemOptionRepository.findAllByCartItemId(cartItem.getId());
            List<CartItemOptionDto> optDtos = savedOpts.stream()
                    .map(option -> new CartItemOptionDto(
                            option.getId(),
                            option.getCartItem().getId(),
                            option.getItemOptionId(),
                            option.getQuantity(),
                            null))
                    .toList();

            Long singleTotal = cartQueryRepository.calculateTotalPriceWithOption(
                    cartItem.getBeverageItemId(), optDtos);
            finalPrice = singleTotal * modifyCartItemDto.changeQuantity();
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
    public List<Long> deleteCartItem(DeleteCartItemDto deleteCartItemDto, Long memberId) {
        Cart cart = validAndCalculatorService.findCartByMemberId(memberId);

        List<Long> deletedIds = new ArrayList<>();

        for(Long cartItemId : deleteCartItemDto.cartItemId()) {
            CartItem cartItem = validAndCalculatorService.findCartItemByCartId(cartItemId);

            if(!cartItem.getCart().getId().equals(cart.getId())) {
                throw new CartException(CartErrorCode.CART_INVALID);}

            cartItemRepository.deleteById(cartItemId);
            deletedIds.add(cartItemId);
        }
        return deletedIds;

    }

    /**
     * //TODO : 조회 서비스 로직이 너무 많은 책임을 가지고 있는거같아, 추후에 간소화 예정
     *  전과 같이 Dto를 활용하여, memberId 별로 1개의 카트를 가지고 있으니,
     *  memberId에 해당하는 cart에 cartItem을 조회해오는 로직
     */
    @Transactional
    public CheckCartItemResponse getCartItem(Long memberId) {

        Cart cart = validAndCalculatorService.findCartByMemberId(memberId);
        List<CartItem> cartItems = cartItemRepository.findAllByCartId(cart.getId());

        List<CheckCartItemDto> checkCartItemDtos = cartItems.stream()
                .map(cartItem -> {
                    ItemType itemType = cartItem.getItemType();
                    String itemName;
                    String imageUrl = cartItem.getImageUrl();

                    if (itemType == ItemType.BEVERAGE) {
                        BeverageItem beverageItem = validAndCalculatorService.findBeverageItemByItemId(cartItem.getBeverageItemId());
                        itemName = beverageItem.getItemNameKo();

                        // 온도에 따른 이미지 처리 로직 간소화
                        imageUrl = (cartItem.getSelectedTemperatures() == BeverageTemperatureOption.HOT ||
                                cartItem.getSelectedTemperatures() == BeverageTemperatureOption.HOT_ONLY)
                                ? beverageItem.getHotImageUrl()
                                : beverageItem.getIceImageUrl();
                    } else {
                        DessertItem dessertItem = validAndCalculatorService.findDessertItemByItemId(cartItem.getDessertItemId());
                        itemName = dessertItem.getDessertItemNameKo();
                        imageUrl = dessertItem.getImageUrl();
                    }

                    List<CheckCartItemOptionDto> options = cartItem.getCartItemOption().stream()
                            .map(option -> {
                                ItemOption itemOption = itemOptionRepository.findById(option.getItemOptionId())
                                        .orElseThrow(() -> new CartException(CartErrorCode.ITEM_OPTION_NOT_FOUND));

                                CheckCartItemOptionDto checkCartItemOptionDto = new CheckCartItemOptionDto(
                                        option.getItemOptionId(),
                                        option.getQuantity()
                                );
                                return checkCartItemOptionDto;
                            }).toList();

                    return CheckCartItemDto.builder()
                            .cartItemId(cartItem.getId())
                            .itemType(itemType)
                            .itemName(itemName)
                            .quantity(cartItem.getQuantity())
                            .imageUrl(imageUrl)
                            .beverageSizeOption(cartItem.getSelectedSizes())
                            .beverageTemperatureOption(cartItem.getSelectedTemperatures())
                            .options(options)
                            .build();
                }).toList();

        return new CheckCartItemResponse(checkCartItemDtos);
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
