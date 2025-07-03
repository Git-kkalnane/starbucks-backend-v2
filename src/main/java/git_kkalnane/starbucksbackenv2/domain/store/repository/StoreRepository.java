package git_kkalnane.starbucksbackenv2.domain.store.repository;

import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @Query("SELECT new git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto(s.id, s.name, s.address, s.phone, s.openingHours, s.hasDriveThrough, s.seatingCapacity, s.latitude, s.longitude, s.imageUrl, s.crowdLevel) FROM Store s")
    List<StoreSimpleDto> findAllSimpleStores();
}
