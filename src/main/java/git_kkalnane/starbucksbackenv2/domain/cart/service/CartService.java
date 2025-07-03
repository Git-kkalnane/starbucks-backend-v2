package git_kkalnane.starbucksbackenv2.domain.cart.service;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartException;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartItemRepository;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.CartRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final BeverageItemRepository beverageItemRepository;




}
