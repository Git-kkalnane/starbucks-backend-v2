package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItemOption;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartOptionService {

    private final CartItemOptionRepository cartItemOptionRepository;

    /**
     * cartItemDto에 cartItemOptionDto가 함께 넘어오기에, optionDto가 null이거나 비어있으면, 빈 배열을 return
     * CartItemOptionDto에 담긴 값을 옵션별로 정리해서 List화
     * 후에 cartItemOptionRepository에 저장한다.
     */
    public void saveCartItemOptions(CartItem cartItem, List<CartItemOptionDto> optionDtos) {
        if (optionDtos == null || optionDtos.isEmpty()) return;

        List<CartItemOption> options = optionDtos.stream()
                .map(dto -> CartItemOption.builder()
                        .cartItem(cartItem)
                        .itemOptionId(dto.itemOptionId())
                        .quantity(dto.quantity())
                        .build())
                .toList();

        cartItemOptionRepository.saveAll(options);
    }
}
