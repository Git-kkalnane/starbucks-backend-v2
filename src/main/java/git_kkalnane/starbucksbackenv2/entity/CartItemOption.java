package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_item_options")
public class CartItemOption {
    @Id
    private Long id;

    @Column(name = "beverage_itme_id")
    private Long beverageItemId;

    @Column(name = "option_name", length = 50)
    private String optionName;

    // Getters and Setters
}
