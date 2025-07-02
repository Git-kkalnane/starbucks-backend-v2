package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item_options")
public class CartItemOption extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    private Long id;

    @Column(name = "beverage_itme_id")
    private Long beverageItemId;

    @Column(name = "option_name", length = 50)
    private String optionName;

    // Getters and Setters
}
