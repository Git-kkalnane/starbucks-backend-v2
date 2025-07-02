package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
