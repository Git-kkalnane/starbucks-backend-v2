package git_kkalnane.starbucksbackenv2.domain.order.domain;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @OneToMany(mappedBy = "orderItem")
    private java.util.List<OrderItemOption> orderItemOptions;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "beverage_id")
    private Long beverageItemId;

    @Column(name = "dessert_id")
    private Long dessertItemId;

    @Column(nullable = false)
    private Long quantity;

    @Column(name = "final_item_price", nullable = false)
    private Long finalItemPrice;

    @Column(name = "item_price", nullable = false)
    private Long itemPrice;

    @Column(name = "shot_quantity")
    private Long shotQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_sizes")
    private BeverageSizeOption selectedSizes;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_temperatures")
    private BeverageTemperatureOption selectedTemperatures;

    // Getters and Setters
}
