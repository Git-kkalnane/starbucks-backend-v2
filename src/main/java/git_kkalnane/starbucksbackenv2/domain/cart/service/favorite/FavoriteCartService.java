package git_kkalnane.starbucksbackenv2.domain.cart.service.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteCartItemDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.favorite.FavoriteSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.FavoriteCartItemResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.response.favorite.FavoriteSimpleResponse;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite.FavoriteCartRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FavoriteCartService {

    private final FavoriteCartRepository favoriteCartRepository;
    private final FavoriteCartItemRepository favoriteCartItemRepository;
    private final FavoriteValidAndCalculatorService favoriteValidAndCalculatorService;
    private final FavoriteCartItemCreateService favoriteCartItemCreateService;
    private final FavoriteCartOptionService favoriteCartOptionService;

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

}
