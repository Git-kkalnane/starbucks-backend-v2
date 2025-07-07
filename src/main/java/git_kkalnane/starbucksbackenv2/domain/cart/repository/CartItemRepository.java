package git_kkalnane.starbucksbackenv2.domain.cart.repository;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
}
