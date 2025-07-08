package git_kkalnane.starbucksbackenv2.domain.notification.aop;


import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.merchant.domain.Merchant;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationTargetType;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationType;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.vo.*;
import git_kkalnane.starbucksbackenv2.domain.notification.event.NotificationEventPublisher;
import git_kkalnane.starbucksbackenv2.domain.notification.event.OrderNotificationSendEvent;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 알림 서비스에 대한 AOP 처리를 담당하는 어스펙트 클래스
 * NotificationService의 sendNotification 메서드 실행 후 이벤트를 발행하고 예외를 로깅합니다.
 */
@Aspect
@Component
@RequiredArgsConstructor
public class NotificationAspect {

    private final NotificationEventPublisher notificationEventPublisher;

    /**
     * 주문 생성 서비스 로직을 수행하는 OrderService.createOrder를 대상으로 포인트 컷을 지정합니다.
     */
    @Pointcut("execution(* git_kkalnane.starbucksbackenv2.domain.order.service.OrderService.createOrder(..))")
    public void notificationServiceMethods() {}

    /**
     * 주문 생성 실행 후 지점에게 알림을 보내는 어드바이스
     * OrderNotificationSendEvent를 발행하여 지점에게 알림을 전송합니다.
     *
     * @param order  주문 생성 후 반환된 주문 엔티티
     */
    @AfterReturning(pointcut = "notificationServiceMethods()", returning = "order")
    public void orderAfter(Order order) {
        Merchant merchant = order.getStore().getMerchant();
        Member member = order.getMember();

        notificationEventPublisher.publish(new OrderNotificationSendEvent(
                this,
                order,
                NotificationSender.of(member.getId()),
                NotificationReceiver.of(merchant.getId()),
                NotificationType.ORDER_CREATED,
                NotificationTargetType.STORE
        ));
    }
}
