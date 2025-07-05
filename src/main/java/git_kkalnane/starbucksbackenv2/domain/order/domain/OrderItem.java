package git_kkalnane.starbucksbackenv2.domain.order.domain;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class OrderItem extends BaseTimeEntity {
    @Builder
    public OrderItem(Long id, Order order, List<OrderItemOption> orderItemOptions, ItemType itemType, String itemName,
        Long beverageItemId, Long dessertItemId, Integer quantity, Long itemPrice, Long finalItemPrice,
        Integer shotQuantity, BeverageSizeOption selectedSize, BeverageTemperatureOption selectedTemperature) {

        this.id = id;
        this.order = order;
        this.orderItemOptions = orderItemOptions;
        this.itemType = itemType;
        this.itemName = itemName;
        this.beverageItemId = beverageItemId;
        this.dessertItemId = dessertItemId;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.finalItemPrice = finalItemPrice;
        this.shotQuantity = shotQuantity;
        this.selectedSize = selectedSize;
        this.selectedTemperature = selectedTemperature;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToMany(mappedBy = "orderItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemOption> orderItemOptions;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "item_name_at_order")
    private String itemName;

    @Column(name = "beverage_id")
    private Long beverageItemId;

    @Column(name = "dessert_id")
    private Long dessertItemId;
    private Integer quantity;
    private Long itemPrice;
    private Long finalItemPrice;
    private Integer shotQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_sizes")
    private BeverageSizeOption selectedSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_temperatures")
    private BeverageTemperatureOption selectedTemperature;



    /**
     * 기존 OrderItem의 속성에 지정된 Order를 할당하여 새 OrderItem을 생성합니다. (주문-주문항목 연관관계 세팅용)
     */
    public static OrderItem withOrder(Order order, OrderItem orderItem) {
        return OrderItem.builder()
            .order(order)
            .orderItemOptions(orderItem.getOrderItemOptions())
            .itemType(orderItem.getItemType())
            .itemName(orderItem.getItemName())
            .beverageItemId(orderItem.getBeverageItemId())
            .dessertItemId(orderItem.getDessertItemId())
            .quantity(orderItem.getQuantity())
            .itemPrice(orderItem.getItemPrice())
            .finalItemPrice(orderItem.getFinalItemPrice())
            .shotQuantity(orderItem.getShotQuantity())
            .selectedSize(orderItem.getSelectedSize())
            .selectedTemperature(orderItem.getSelectedTemperature())
            .build();
    }

    public static OrderItem withOrder(Long orderId, OrderItem orderItem) {
        Order _order = Order.builder()
            .id(orderId)
            .build();
        return OrderItem.builder()
            .order(_order)
            .orderItemOptions(orderItem.getOrderItemOptions())
            .itemType(orderItem.getItemType())
            .itemName(orderItem.getItemName())
            .beverageItemId(orderItem.getBeverageItemId())
            .dessertItemId(orderItem.getDessertItemId())
            .quantity(orderItem.getQuantity())
            .itemPrice(orderItem.getItemPrice())
            .finalItemPrice(orderItem.getFinalItemPrice())
            .shotQuantity(orderItem.getShotQuantity())
            .selectedSize(orderItem.getSelectedSize())
            .selectedTemperature(orderItem.getSelectedTemperature())
            .build();
    }
}