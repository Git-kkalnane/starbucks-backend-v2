package git_kkalnane.starbucksbackenv2.domain.cart.service.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.CheckFavoriteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.CheckFavoriteCartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.CheckFavoriteCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.FavoriteCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.FavoriteSimpleResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class FavoriteCartService {

    private final FavoriteCartRepository favoriteCartRepository;
    private final FavoriteCartItemRepository favoriteCartItemRepository;
    private final FavoriteValidAndCalculatorService favoriteValidAndCalculatorService;
    private final FavoriteCartItemCreateService favoriteCartItemCreateService;
    private final FavoriteCartOptionService favoriteCartOptionService;
    private final ItemOptionRepository itemOptionRepository;

    @Transactional
    public FavoriteSimpleResponse createFavoriteCartItem(FavoriteSimpleDto favoriteSimpleDto, Long memberId) {
        Member member = favoriteValidAndCalculatorService.findByMemberId(memberId);
        FavoriteCart favoriteCart = favoriteValidAndCalculatorService.findFavoriteCartByMemberId(memberId);

        favoriteValidAndCalculatorService.validateFavoriteSimpleDto(favoriteSimpleDto);

        Long singleTotal = favoriteValidAndCalculatorService.calculateSingleTotal(favoriteSimpleDto);

        FavoriteCartItem favoriteCartItem = favoriteCartItemCreateService.createFavoriteCartItem(favoriteCart, favoriteSimpleDto, singleTotal);
        favoriteCartItem = favoriteCartItemRepository.save(favoriteCartItem);

        return FavoriteSimpleResponse.of(favoriteCartItem);
    }

    @Transactional
    public FavoriteCartItemResponse createFavoriteCartItemWithOption(Long memberId, FavoriteCartItemDto favoriteCartItemDto) {
        Member member = favoriteValidAndCalculatorService.findByMemberId(memberId);
        FavoriteCart favoriteCart = favoriteValidAndCalculatorService.findFavoriteCartByMemberId(memberId);

        Long singleTotal = favoriteValidAndCalculatorService.calculateSingleTotalWithOption(favoriteCartItemDto);

        FavoriteCartItem favoriteCartItem = favoriteCartItemCreateService.createFavoriteCartItems(favoriteCart, favoriteCartItemDto, singleTotal);
        favoriteCartItem = favoriteCartItemRepository.save(favoriteCartItem);

        favoriteCartOptionService.saveCartItemOptions(favoriteCartItem, favoriteCartItemDto.cartItemOptions());

        return FavoriteCartItemResponse.of(favoriteCartItem, favoriteCartItemDto.cartItemOptions());
    }

    @Transactional
    public Long deleteCartItem(Long favoriteCartItemId, Long memberId) {
        FavoriteCart favoriteCart = favoriteValidAndCalculatorService.findFavoriteCartByMemberId(memberId);
        FavoriteCartItem favoriteCartItem = favoriteValidAndCalculatorService.findCartItemByCartId(favoriteCartItemId);

        favoriteCartItemRepository.deleteById(favoriteCartItemId);

        return favoriteCartItem.getId();


    }

    @Transactional
    public CheckFavoriteCartItemResponse getFavoriteCartItem(Long memberId) {
        FavoriteCart favoriteCart = favoriteValidAndCalculatorService.findFavoriteCartByMemberId(memberId);
        List<FavoriteCartItem> cartItems = favoriteCartItemRepository.findAllByFavoriteCartId(favoriteCart.getId());

        List<CheckFavoriteCartItemDto> checkFavoriteCartItemDto = cartItems.stream()
                .map(cartItem -> {
                    ItemType itemType = cartItem.getItemType();
                    Long itemId;
                    String itemName;
                    String imageUrl;

                    if(itemType == ItemType.BEVERAGE) {
                        BeverageItem beverageItem = favoriteValidAndCalculatorService.findBeverageItemByItemId(cartItem.getBeverageItemId());
                        itemId = beverageItem.getId();
                        itemName = beverageItem.getItemNameKo();

                        imageUrl = (cartItem.getSelectedTemperatures() == BeverageTemperatureOption.HOT ||
                                cartItem.getSelectedTemperatures() == BeverageTemperatureOption.HOT_ONLY)
                                ? beverageItem.getHotImageUrl() :
                                beverageItem.getIceImageUrl();
                    } else {
                        DessertItem dessertItem = favoriteValidAndCalculatorService.findDessertItemByItemId(cartItem.getDessertItemId());
                        itemId = dessertItem.getId();
                        itemName = dessertItem.getDessertItemNameKo();
                        imageUrl = dessertItem.getImageUrl();
                    }

                    List<CheckFavoriteCartItemOptionDto> options = cartItem.getFavoriteCartItemOption().stream()
                            .map(option -> {
                                ItemOption itemOption = itemOptionRepository.findById(option.getItemOptionId())
                                        .orElseThrow(() -> new CartException(CartErrorCode.ITEM_OPTION_NOT_FOUND));

                                CheckFavoriteCartItemOptionDto checkFavoriteCartItemOptionDto = new CheckFavoriteCartItemOptionDto(
                                        option.getItemOptionId(),
                                        option.getQuantity()
                                );
                                return checkFavoriteCartItemOptionDto;
                            }).toList();
                    Long totalPrice = calculateCartTotalPrice(favoriteCart.getId(), memberId);

                    return CheckFavoriteCartItemDto.builder()
                            .favoriteCartItemId(cartItem.getId())
                            .itemId(itemId)
                            .totalPrice(totalPrice)
                            .itemType(itemType)
                            .itemName(itemName)
                            .quantity(cartItem.getQuantity())
                            .imageUrl(imageUrl)
                            .beverageSizeOption(cartItem.getSelectedSizes())
                            .beverageTemperatureOption(cartItem.getSelectedTemperatures())
                            .options(options)
                            .build();
                }).toList();
        return new CheckFavoriteCartItemResponse(checkFavoriteCartItemDto);
    }

    @Transactional
    public FavoriteCart createFavoriteCartForMember(Member member) {
        // 이미 존재하는지 확인
        if (favoriteCartRepository.existsByMemberId(member.getId())) {
            throw new CartException(CartErrorCode.CART_ALREADY_EXISTS);
        }

        FavoriteCart favoritecart = FavoriteCart.builder()
                .member(member)
                .build();

        return favoriteCartRepository.save(favoritecart);
    }

    public Long calculateCartTotalPrice(Long cartId, Long memberId) {
        FavoriteCart favoriteCart = favoriteValidAndCalculatorService.findFavoriteCartByMemberId(memberId);

        return favoriteCart.getFavoriteCartItems().stream()
                .mapToLong(cartItem -> {
                    Long itemTotal = cartItem.getItemPrice() * cartItem.getQuantity(); // 옵션 포함된 가격
                    System.out.println("itemTotal = " + itemTotal);
                    return itemTotal;
                }).sum();
    }

}
