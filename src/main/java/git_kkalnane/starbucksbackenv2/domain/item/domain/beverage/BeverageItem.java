package git_kkalnane.starbucksbackenv2.domain.item.domain.beverage;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;

@Entity
@Table(name = "beverage_items")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder

public class BeverageItem extends BaseTimeEntity {
    @Id
    private Long id;

    @Column(name = "name_ko", length = 50, unique = true, nullable = false)
    private String itemNameKo;

    @Column(name = "name_en", length = 50, unique = true, nullable = false)
    private String itemNameEn;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "is_coffee")
    private Boolean isCoffee = false;

    @Column(name = "hot_img_url", length = 254)
    private String hotImageUrl;

    @Column(name = "ice_img_url", length = 254)
    private String iceImageUrl;

    @Column(name = "shot_name", length = 254)
    private String shotName;

    @ElementCollection
    @CollectionTable(
            name = "beverage_supported_sizes",
            joinColumns = @JoinColumn(name = "beverage_item_id")
    )
    @Column(name = "size_option")
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private Set<BeverageSizeOption> supportedSizes= new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "supported_temperatures")
    private BeverageTemperatureOption supportedTemperatures;



    // Getters and Setters
}
