package git_kkalnane.starbucksbackenv2.domain.cart.repository;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.CartQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, CartQueryRepository {
    boolean existsByMemberId(Long memberId);
}
