package git_kkalnane.starbucksbackenv2.domain.item.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "item_options")
public class ItemOption extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "beverage_itme_id")
    private Long beverageItemId;

    @Column(name = "option_name", length = 50)
    private String optionName;

    // Getters and Setters
}
