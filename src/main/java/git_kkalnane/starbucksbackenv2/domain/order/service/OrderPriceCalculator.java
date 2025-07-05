package git_kkalnane.starbucksbackenv2.domain.order.service;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItem;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class OrderPriceCalculator {
    /**
     * 모든 주문 아이템의 최종 금액을 합산하여 총 주문 금액을 계산합니다.
     *
     * @param orderItems 주문 아이템 목록
     * @return 주문의 총 금액
     */
    public Long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(OrderItem::getFinalItemPrice)
                .sum();
    }
}
