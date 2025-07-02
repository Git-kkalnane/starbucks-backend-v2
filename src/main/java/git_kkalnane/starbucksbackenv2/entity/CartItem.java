package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "cart_favorit_id")
    private CartFavoritItem cartFavoritItem;

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

    @Column(name = "image_url")
    private String imageUrl;

    // Getters and Setters
}
