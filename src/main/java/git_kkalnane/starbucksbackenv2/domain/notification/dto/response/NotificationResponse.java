package git_kkalnane.starbucksbackenv2.domain.notification.dto.response;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {
    private String eventId;
    private Long senderId;
    private Long receiverId;
    private String title;
    private String message;
    private String notificationType;
    private String notificationTargetType;
}
