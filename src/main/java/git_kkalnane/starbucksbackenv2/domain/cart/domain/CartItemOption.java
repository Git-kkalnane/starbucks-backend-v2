package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item_options")
public class CartItemOption extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    private Long id;

    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "item_option_id")
    private Long itemOptionId;

    @Column(name = "quantity")
    private Long quantity;

    // Getters and Setters
}
