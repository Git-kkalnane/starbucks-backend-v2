package git_kkalnane.starbucksbackenv2.domain.item.domain.beverage;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "beverage_items")
public class BeverageItem extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    private Long id;

    @Column(name = "item_name_ko", length = 50, unique = true, nullable = false)
    private String itemNameKo;

    @Column(name = "item_name_en", length = 50, unique = true, nullable = false)
    private String itemNameEn;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "is_coffee")
    private Boolean isCoffee = false;

    @Column(name = "hot_image_url", length = 254)
    private String hotImageUrl;

    @Column(name = "ice_image_url", length = 254)
    private String iceImageUrl;

    @Column(name = "shot_name", length = 254)
    private String shotName;

    @Enumerated(EnumType.STRING)
    @Column(name = "supported_sizes")
    private BeverageSizeOption supportedSizes;

    @Enumerated(EnumType.STRING)
    @Column(name = "supported_temperatures")
    private BeverageTemperatureOption supportedTemperatures;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
}
