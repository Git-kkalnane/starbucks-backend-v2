package git_kkalnane.starbucksbackenv2.domain.cart.repository.favorite;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.FavoriteCartItemOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteCartItemOptionRepository extends JpaRepository<FavoriteCartItemOption, Long> {
}
