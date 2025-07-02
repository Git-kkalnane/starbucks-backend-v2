package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.PayCard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayCardRepository extends JpaRepository<PayCard, Long> {
}
