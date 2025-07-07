package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "quantity", nullable = false)
    private int quantity = 1;

    @Column(name = "final_item_price", nullable = false)
    private Long finalItemPrice;

    @Column(name = "item_price", nullable = false)
    private Long itemPrice;

    @Column(name = "shot_quantity")
    private Long shotQuantity = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_sizes")
    private BeverageSizeOption selectedSizes;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_temperatures")
    private BeverageTemperatureOption selectedTemperatures;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "cartItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemOption> cartItemOption;

    public void changeQuantity(int changeQuantity) {
        this.quantity = changeQuantity;
    }
    public void setFinalItemPrice(Long finalItemPrice) {
        this.finalItemPrice = finalItemPrice;
    }

    // Getters and Setters
}
