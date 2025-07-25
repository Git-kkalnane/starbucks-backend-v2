package git_kkalnane.starbucksbackenv2.domain.item.domain.beverage;

import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "beverage_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
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
    @Builder.Default
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
