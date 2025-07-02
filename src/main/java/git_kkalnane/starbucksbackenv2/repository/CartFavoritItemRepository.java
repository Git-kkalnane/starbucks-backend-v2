package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.CartFavoritItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartFavoritItemRepository extends JpaRepository<CartFavoritItem, Long> {
}
