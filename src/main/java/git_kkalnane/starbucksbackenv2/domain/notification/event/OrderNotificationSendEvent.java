package git_kkalnane.starbucksbackenv2.domain.notification.event;

import git_kkalnane.starbucksbackenv2.domain.notification.domain.*;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.vo.*;
import git_kkalnane.starbucksbackenv2.domain.notification.dto.response.OrderNotificationSendBeverageItemResponse;
import git_kkalnane.starbucksbackenv2.domain.notification.dto.response.OrderNotificationSendDessertItemResponse;
import git_kkalnane.starbucksbackenv2.domain.notification.dto.response.OrderNotificationSendResponse;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderNotificationSendEvent extends ApplicationEvent {

    private OrderNotificationSendResponse item;
    private String title;
    private String message;
    private NotificationSender sender;
    private NotificationReceiver receiver;
    private NotificationTargetType notificationTargetType;
    private NotificationType notificationType;

    public OrderNotificationSendEvent(Object object,
                                      Order order,
                                      NotificationSender sender,
                                      NotificationReceiver receiver,
                                      NotificationType notificationType, NotificationTargetType notificationTargetType) {
        super(object);
        init(order);
        this.title = notificationType.getTitle();
        this.message = notificationType.getMessage();
        this.sender = sender;
        this.receiver = receiver;
        this.notificationType = notificationType;
        this.notificationTargetType = notificationTargetType;
    }


    /**
     * Merchant 에게 보낼 응답 구성을 위한 init 메서드
     *
     * @param order  주문 생성 후 반환된 주문 엔티티
     */
    private void init(Order order) {
        List<OrderNotificationSendBeverageItemResponse> beverageItems = new ArrayList<>();
        List<OrderNotificationSendDessertItemResponse> dessertItems = new ArrayList<>();

        order.getOrderItems().forEach(orderItem -> {
                    if (orderItem.getBeverageItemId() != null) {
                        beverageItems.add(OrderNotificationSendBeverageItemResponse.of(orderItem.getBeverageItemId()));
                    } else {
                        dessertItems.add(OrderNotificationSendDessertItemResponse.of(orderItem.getDessertItemId()));
                    }
                }
        );

        this.item = OrderNotificationSendResponse.builder()
                .orderId(order.getId())
                .orderNumber(order.getOrderNumber())
                .expectedPickupTime(order.getExpectedPickupTime())
                .requestMemo(order.getRequestMemo())
                .pickupType(order.getPickupType())
                .storeId(order.getStore().getId())
                .memberId(order.getMember().getId())
                .memberName(order.getMember().getName())
                .beverageItems(beverageItems)
                .dessertItems(dessertItems)
                .build();
    }
}
