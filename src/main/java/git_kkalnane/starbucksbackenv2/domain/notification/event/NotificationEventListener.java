package git_kkalnane.starbucksbackenv2.domain.notification.event;


import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationTargetType;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationType;
import git_kkalnane.starbucksbackenv2.domain.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 알림 이벤트를 처리하는 리스너 클래스
 * NotificationFlippedSendEvent 이벤트를 비동기로 처리하여 알림을 전송합니다.
 */
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final NotificationService notificationService;

    @EventListener(OrderNotificationSendEvent.class)
    @Async(value = "asyncThreadPool")
    public void handle(OrderNotificationSendEvent event) {

        // 멤버에게 주문 접수 알림 전송
        notificationService.sendNotificationWithOrder(
                event.getItem(),
                NotificationType.ORDER_ACCEPTED.getTitle(),
                NotificationType.ORDER_ACCEPTED.getMessage(
                        event.getItem().getMemberName(),
                        String.valueOf(event.getItem().getOrderNumber())
                ),
                event.getReceiver().value(),
                event.getSender().value(),
                NotificationType.ORDER_ACCEPTED,
                NotificationTargetType.MEMBER
        );

        // 지점에게 주문 발생 알림 전송
        notificationService.sendNotificationWithOrder(
                event.getItem(),
                event.getTitle(),
                event.getMessage(),
                event.getSender().value(),
                event.getReceiver().value(),
                event.getNotificationType(),
                event.getNotificationTargetType()
        );
    }
}
