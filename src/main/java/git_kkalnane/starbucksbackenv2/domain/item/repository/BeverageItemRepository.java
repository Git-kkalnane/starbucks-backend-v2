package git_kkalnane.starbucksbackenv2.domain.item.repository;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface BeverageItemRepository extends JpaRepository<BeverageItem, Long> {
    // id 오름차순 전체 목록 조회
@Query("SELECT DISTINCT b FROM BeverageItem b LEFT JOIN FETCH b.supportedSizes")
List<BeverageItem> findAllWithSupportedSizes();
}
