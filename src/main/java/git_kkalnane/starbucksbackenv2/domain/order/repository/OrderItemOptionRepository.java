package git_kkalnane.starbucksbackenv2.domain.order.repository;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItemOption;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemOptionRepository extends JpaRepository<OrderItemOption, Long> {
    List<OrderItemOption> findByOrderItemIdIn(List<Long> orderItemIds);



}
