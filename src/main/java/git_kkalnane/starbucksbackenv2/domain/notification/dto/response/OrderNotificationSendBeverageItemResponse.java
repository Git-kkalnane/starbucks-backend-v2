package git_kkalnane.starbucksbackenv2.domain.notification.dto.response;


import lombok.Getter;


@Getter
public class OrderNotificationSendBeverageItemResponse {
    private Long beverage;

    private OrderNotificationSendBeverageItemResponse(Long beverage) {
        this.beverage = beverage;
    }

    public static OrderNotificationSendBeverageItemResponse of(Long beverage) {
        return new OrderNotificationSendBeverageItemResponse(beverage);
    }
}
