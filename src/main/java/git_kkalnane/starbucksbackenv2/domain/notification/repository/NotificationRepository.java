package git_kkalnane.starbucksbackenv2.domain.notification.repository;

import git_kkalnane.starbucksbackenv2.domain.notification.domain.Notification;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationTargetType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findAllByReceiverIdAndNotificationTargetType
            (Long receiverId, NotificationTargetType notificationTargetType,Pageable pageable);
}
