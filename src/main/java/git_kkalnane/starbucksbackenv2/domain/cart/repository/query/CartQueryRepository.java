package git_kkalnane.starbucksbackenv2.domain.cart.repository.query;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.dto.request.CartItemOptionDto;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartQueryRepository {

    Optional<Cart> findByMemberId(Long memberId);
    Long calculateTotalPriceWithOption(Long itemId, List<CartItemOptionDto> optionDtos);
    Long calculatePrice(Long itemId);

}
