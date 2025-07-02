package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
