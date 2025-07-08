package git_kkalnane.starbucksbackenv2.domain.item.repository;

import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertItemRepository extends JpaRepository<DessertItem, Long> {
    Optional<DessertItem> findById(Long id);
}
