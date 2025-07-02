package git_kkalnane.starbucksbackenv2.domain.pointcard.repository;

import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointCardRepository extends JpaRepository<PointCard, Long> {
}
