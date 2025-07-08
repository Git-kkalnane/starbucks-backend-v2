package git_kkalnane.starbucksbackenv2.domain.item.repository;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BeverageItemQueryRepository {
    Page<BeverageItem> findAllWithSupportedSizes(Pageable pageable);
}
