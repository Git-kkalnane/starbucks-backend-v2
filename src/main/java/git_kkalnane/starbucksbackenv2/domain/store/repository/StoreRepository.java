package git_kkalnane.starbucksbackenv2.domain.store.repository;

import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
}
