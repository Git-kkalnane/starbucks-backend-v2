package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.BeverageItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeverageItemRepository extends JpaRepository<BeverageItem, Long> {
}
