package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item_options")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartItemOption extends BaseTimeEntity {
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
