package git_kkalnane.starbucksbackenv2.domain.order.repository;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderDailyCounter;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderDailyCounterId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDailyCounterRepository extends JpaRepository<OrderDailyCounter, OrderDailyCounterId> {
}