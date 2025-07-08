package git_kkalnane.starbucksbackenv2.domain.notification.dto.response;


import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class OrderNotificationSendResponse {
    private List<OrderNotificationSendBeverageItemResponse> beverageItems;
    private List<OrderNotificationSendDessertItemResponse> dessertItems;
    private Long orderId;
    private String orderNumber;
    private PickupType pickupType;
    private String requestMemo;
    private LocalDateTime expectedPickupTime;
    private String memberName;
    private Long memberId;
    private Long storeId;
}
