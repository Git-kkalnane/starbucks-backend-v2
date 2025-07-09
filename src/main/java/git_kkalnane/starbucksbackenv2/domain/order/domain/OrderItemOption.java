package git_kkalnane.starbucksbackenv2.domain.order.domain;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItemOption extends BaseTimeEntity {

    @Builder
    public OrderItemOption(Long id, OrderItem orderItem, Long itemOptionId, int quantity) {
        this.id = id;
        this.orderItem = orderItem;
        this.itemOptionId = itemOptionId;
        this.quantity = quantity;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_option_id")
    private ItemOption itemOption;

    @Column
    private int quantity;


    static public OrderItemOption withOrderItem(OrderItem orderItem, OrderItemOption orderItemOption) {

        return OrderItemOption.builder()
            .id(orderItemOption.getId())
            .orderItem(orderItem)
            .itemOptionId(orderItemOption.getItemOptionId())
            .quantity(orderItemOption.getQuantity())
            .build();
    }

    static public OrderItemOption withOrderItem(Long orderItemId, OrderItemOption orderItemOption) {
         OrderItem _orderItem = OrderItem.builder().id(orderItemId).build();

        return OrderItemOption.builder()
            .id(orderItemOption.getId())
            .orderItem(_orderItem)
            .itemOptionId(orderItemOption.getItemOptionId())
            .quantity(orderItemOption.getQuantity())
            .build();
    }
}
