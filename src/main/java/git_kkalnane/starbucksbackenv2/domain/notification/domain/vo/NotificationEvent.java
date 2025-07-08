package git_kkalnane.starbucksbackenv2.domain.notification.domain.vo;


import git_kkalnane.starbucksbackenv2.domain.notification.common.exception.NotificationErrorCode;
import git_kkalnane.starbucksbackenv2.domain.notification.common.exception.NotificationException;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationTargetType;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationType;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

@Embeddable
@EqualsAndHashCode
public class NotificationEvent {
    private final String id;

    protected NotificationEvent() {this.id = null;}

    private NotificationEvent(String id) {
        this.id = id;
    }

    public String value() {
        return id;
    }

    public static NotificationEvent of(
                                Long receiverId,
                                 NotificationTargetType notificationTargetType,
                                 NotificationType notificationType) {
        return NotificationEvent.of("%s_%s_%s_%s"
                .formatted(
                        notificationTargetType.name(),
                        receiverId,
                        notificationType.name(),
                        System.currentTimeMillis()));
    }

    public static NotificationEvent of(String eventId) {
        validate(eventId);
        return new NotificationEvent(eventId);
    }

    private static void validate(String eventId) {
        if (StringUtils.hasText(eventId)) {
            throw new NotificationException(NotificationErrorCode.EVENT_ID_IS_NOT_EMPTY, eventId);
        }
    }
}
