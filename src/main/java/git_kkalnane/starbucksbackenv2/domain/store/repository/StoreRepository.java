package git_kkalnane.starbucksbackenv2.domain.store.repository;

import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    /**
     * 매장 ID로 Store 엔티티를 조회하여, StoreSimpleDto로 변환해 반환.
     * <p>
     * - 복잡한 연관 엔티티 없이, 매장의 기본 정보만 필요한 경우에 사용. <br/>
     * - Store 엔티티 전체가 아닌, 필요한 필드만 추출하여 가볍게 조회 가능. <br/>
     * - 매장이 존재하지 않으면 Optional.empty()를 반환.<br/>
     *
     * @param id 조회할 매장의 ID
     * @return 매장 기본 정보가 담긴 StoreSimpleDto (존재하지 않으면 Optional.empty())
     */
    @Query("SELECT new git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto(\n" +
            "s.id, s.name, s.address, s.phone, s.openingHours, s.hasDriveThrough, s.seatingCapacity, s.latitude, s.longitude, s.imageUrl, s.crowdLevel) \n" +
            "FROM Store s WHERE s.id = :id")
    Optional<StoreSimpleDto> findSimpleDtoById(@Param("id") Long id);

}
