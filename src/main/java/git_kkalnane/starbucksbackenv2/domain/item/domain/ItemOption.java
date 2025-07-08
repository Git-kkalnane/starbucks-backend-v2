package git_kkalnane.starbucksbackenv2.domain.item.domain;

import jakarta.persistence.*;
import lombok.*;

/**
 * ItemOption
 * BeverageItem에서만 사용됩니다.
 * 
 * @author joungmin
 * @version 1.0
 * @since 2025-07-02
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "item_options")
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
