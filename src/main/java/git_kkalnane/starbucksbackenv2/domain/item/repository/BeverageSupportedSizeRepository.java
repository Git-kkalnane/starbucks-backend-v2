package git_kkalnane.starbucksbackenv2.domain.item.repository;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSupportedSize;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface BeverageSupportedSizeRepository extends JpaRepository<BeverageSupportedSize, Long> {
    // 이름으로 사이즈 조회
    Optional<BeverageSupportedSize> findByName(String name);

    // 이름+용량으로 사이즈 조회
    Optional<BeverageSupportedSize> findByNameAndVolume(String name, Integer volume);

    // 여러 이름으로 사이즈 리스트 조회
    List<BeverageSupportedSize> findAllByNameIn(List<String> names);

    // JPQL: 특정 음료가 지원하는 사이즈명 리스트
    @Query("SELECT s.name FROM BeverageSupportedSize s JOIN s.items i WHERE i.id = :itemId")
    List<String> findSizeNamesByBeverageItemId(@Param("itemId") Long itemId);

    // Native: 특정 사이즈를 지원하는 음료 id 리스트
    @Query(value = "SELECT bis.beverage_item_id FROM beverage_item_supported_sizes bis WHERE bis.supported_size_id = :sizeId", nativeQuery = true)
    List<Long> findBeverageItemIdsBySupportedSizeId(@Param("sizeId") Long sizeId);

}
