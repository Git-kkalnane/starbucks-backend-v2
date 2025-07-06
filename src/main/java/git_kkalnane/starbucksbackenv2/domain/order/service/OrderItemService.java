package git_kkalnane.starbucksbackenv2.domain.order.service;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItem;
import git_kkalnane.starbucksbackenv2.domain.order.repository.OrderItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private final OrderItemRepository orderItemRepository;


    @Transactional
    public OrderItem saveOrderItem(Long orderId, OrderItem orderItem) {
        OrderItem newOrderItem = OrderItem.withOrder(orderId, orderItem);
        OrderItem saveOrderItem = orderItemRepository.save(newOrderItem);
        return saveOrderItem;
    }

    /**
     * 여러 OrderItem을 한 번에 저장하고, 저장된 id 리스트를 반환합니다.
     */
    @Transactional
    public List<OrderItem> saveOrderItems(Long orderId, List<OrderItem> orderItems) {
        List<OrderItem> newOrderItems = orderItems.stream()
                .map(item -> OrderItem.withOrder(orderId, item))
                .toList();
        List<OrderItem> saveOrderItems = orderItemRepository.saveAll(newOrderItems);
        return saveOrderItems;
    }

}
