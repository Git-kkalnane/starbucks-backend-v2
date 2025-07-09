package git_kkalnane.starbucksbackenv2.domain.notification.repository;

import git_kkalnane.starbucksbackenv2.domain.notification.domain.OrderNotification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderNotificationRepository extends JpaRepository<OrderNotification, Long> {
}
