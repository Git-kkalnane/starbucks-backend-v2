package git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCart;
import git_kkalnane.starbucksbackenv2.domain.cart.repository.query.FavoriteCartQueryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCartRepository extends JpaRepository<FavoriteCart, Long>, FavoriteCartQueryRepository {
    boolean existsByMemberId(Long memberId);
}
