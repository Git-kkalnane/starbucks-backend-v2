package git_kkalnane.starbucksbackenv2.domain.item.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * ItemOption
 * BeverageItem 에서만 사용됩니다.
 * 
 * @author joungmin
 * @version 1.0
 * @since 2025-07-02
 */
@Entity
@Getter
@Table(name = "item_options")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemOption extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "option_price")
    private Integer optionPrice;
}
