package git_kkalnane.starbucksbackenv2.domain.notification.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderNotificationSendRequest {
    private Long orderId;
    private Long senderId;
    private Long receiverId;
    private String notificationType;
    private String notificationTargetType;
}
