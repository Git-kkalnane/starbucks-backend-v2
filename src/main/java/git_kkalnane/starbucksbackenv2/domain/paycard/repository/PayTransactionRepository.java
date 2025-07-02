package git_kkalnane.starbucksbackenv2.domain.paycard.repository;

import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PayTransactionRepository extends JpaRepository<PayTransaction, Long> {
}
