package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {
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

    @Column(name = "beverage_item_id")
    private Long beverageItemId;

    @Column(name = "dessert_item_id")
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
    private BeverageSupportedSizes selectedSizes;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_temperatures")
    private BeverageSupportedTemperatureOptions selectedTemperatures;

    // Getters and Setters
}
