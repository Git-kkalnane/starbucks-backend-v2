package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "favorite_cart_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FavoriteCartItem extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "favorite_cart_id")
    private FavoriteCart favoriteCart;

    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name ="item_name")
    private String itemName;

    @Column(name = "item_description")
    private String itemDescription;

    @Column(name = "beverage_item_id")
    private Long beverageItemId;

    @Column(name = "dessert_item_id")
    private Long dessertItemId;

    @Column(name = "quantity", nullable = false)
    @Builder.Default
    private int quantity = 1;

    @Column(name = "item_price", nullable = false)
    private Long itemPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_sizes")
    private BeverageSizeOption selectedSizes;

    @Enumerated(EnumType.STRING)
    @Column(name = "selected_temperatures")
    private BeverageTemperatureOption selectedTemperatures;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "favoriteCartItem",cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FavoriteCartItemOption> favoriteCartItemOption = new ArrayList<>();

    @Column(name = "is_deleted")
    private Boolean isDeleted;

}
