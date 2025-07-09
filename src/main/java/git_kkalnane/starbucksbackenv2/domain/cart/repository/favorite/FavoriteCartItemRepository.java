package git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteCartItemRepository extends JpaRepository<FavoriteCartItem, Long> {
    List<FavoriteCartItem> findAllByFavoriteCartId(Long favoriteCartId);
}
