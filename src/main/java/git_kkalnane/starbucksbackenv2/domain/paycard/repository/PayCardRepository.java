package git_kkalnane.starbucksbackenv2.domain.paycard.repository;

import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayCardRepository extends JpaRepository<PayCard, Long> {
}
