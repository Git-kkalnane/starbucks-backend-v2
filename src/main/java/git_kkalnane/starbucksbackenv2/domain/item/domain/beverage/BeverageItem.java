package git_kkalnane.starbucksbackenv2.domain.item.domain.beverage;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;

@Entity
@Table(name = "beverage_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BeverageItem extends BaseTimeEntity {
    @Builder
    BeverageItem(Long id, String itemNameKo, String itemNameEn, String description, Integer price, Boolean isCoffee, String hotImageUrl, String iceImageUrl, String shotName, List<BeverageSupportedSize> supportedSizes, BeverageTemperatureOption supportedTemperatures) {
        this.id = id;
        this.itemNameKo = itemNameKo;
        this.itemNameEn = itemNameEn;
        this.description = description;
        this.price = price;
        this.isCoffee = isCoffee;
        this.hotImageUrl = hotImageUrl;
        this.iceImageUrl = iceImageUrl;
        this.shotName = shotName;
        this.supportedSizes = supportedSizes;
        this.supportedTemperatures = supportedTemperatures;
    }

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

    @ManyToMany
    @JoinTable(
        name = "beverage_item_supported_sizes",
        joinColumns = @JoinColumn(name = "beverage_item_id"),
        inverseJoinColumns = @JoinColumn(name = "supported_size_id")
    )
    private List<BeverageSupportedSize> supportedSizes;

    @Enumerated(EnumType.STRING)
    @Column(name = "supported_temperatures")
    private BeverageTemperatureOption supportedTemperatures;


}
