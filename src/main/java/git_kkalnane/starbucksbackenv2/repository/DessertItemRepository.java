package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.DessertItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DessertItemRepository extends JpaRepository<DessertItem, Long> {
}
