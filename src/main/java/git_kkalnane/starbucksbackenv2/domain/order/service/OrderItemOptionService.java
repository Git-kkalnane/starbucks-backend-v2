package git_kkalnane.starbucksbackenv2.domain.order.service;


import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.order.common.exception.OrderErrorCode;
import git_kkalnane.starbucksbackenv2.domain.order.common.exception.OrderException;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItemOption;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.SelectedItemOptionRequest;
import git_kkalnane.starbucksbackenv2.domain.order.repository.OrderItemOptionRepository;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderItemOptionService {

    private final OrderItemOptionRepository orderItemOptionRepository;
    private final ItemOptionRepository itemOptionRepository;


    @Transactional
    public OrderItemOption saveOrderItemOption(Long orderItemId, OrderItemOption orderItemOption) {
        OrderItemOption newOrderItemOption = OrderItemOption.withOrderItem(orderItemId, orderItemOption);
        return orderItemOptionRepository.save(newOrderItemOption);
    }

    /**
     * 여러 OrderItem을 한 번에 저장하고, 저장된 id 리스트를 반환합니다.
     */
    @Transactional
    public List<OrderItemOption> saveOrderItemsOptions(Long orderItemId, List<OrderItemOption> orderItemOptions) {
        List<OrderItemOption> newOrderItems = orderItemOptions.stream()
            .map(item -> OrderItemOption.withOrderItem(orderItemId, item))
            .toList();
        return   orderItemOptionRepository.saveAll(newOrderItems);
    }

    public List<OrderItemOption> generateOrderItemOptions(List<SelectedItemOptionRequest> options) {
        List<OrderItemOption> orderItemOptions = new ArrayList<>();

        options.forEach(option -> {
            ItemOption itemOption = itemOptionRepository.findById(option.itemOptionId())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ITEM_OPTION_NOT_FOUND));

           OrderItemOption _order = OrderItemOption.builder()
                .itemOptionId(option.itemOptionId())
                .quantity(option.quantity())
                .build();

           orderItemOptions.add(_order);

        });

        return orderItemOptions;
    }

}
