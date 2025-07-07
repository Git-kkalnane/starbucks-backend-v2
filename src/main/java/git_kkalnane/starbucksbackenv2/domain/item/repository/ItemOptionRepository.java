package git_kkalnane.starbucksbackenv2.domain.item.repository;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
    @Query("SELECT io.name FROM ItemOption io WHERE io.id = :id")
    String findNameById(@Param("id") Long id);
}
