package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
