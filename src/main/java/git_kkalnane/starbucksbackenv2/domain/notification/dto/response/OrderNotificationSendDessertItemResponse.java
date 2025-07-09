package git_kkalnane.starbucksbackenv2.domain.notification.dto.response;


import lombok.Getter;

@Getter
public class OrderNotificationSendDessertItemResponse {
    private Long dessert;

    private OrderNotificationSendDessertItemResponse(Long dessert) {
        this.dessert = dessert;
    }

    public static OrderNotificationSendDessertItemResponse of(Long dessert) {
        return new OrderNotificationSendDessertItemResponse(dessert);
    }
}
