package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.PayTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayTransactionRepository extends JpaRepository<PayTransaction, Long> {
}
