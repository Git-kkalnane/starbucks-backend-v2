package git_kkalnane.starbucksbackenv2.domain.item.domain;

import jakarta.persistence.*;

/**
 * ItemOption
 * BeverageItem에서만 사용됩니다.
 * 
 * @author joungmin
 * @version 1.0
 * @since 2025-07-02
 */
@Entity
@Table(name = "item_options")
public class ItemOption extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId; // 현재 기획은 BeverageItem.id만 적용
    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "option_price")
    private Integer optionPrice;
}
