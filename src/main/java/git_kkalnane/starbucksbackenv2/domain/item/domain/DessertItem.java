package git_kkalnane.starbucksbackenv2.domain.item.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "dessert_items")
public class DessertItem extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dessert_item_name_ko", length = 50, unique = true, nullable = false)
    private String dessertItemNameKo;

    @Column(name = "dessert_item_name_en", length = 50, unique = true, nullable = false)
    private String dessertItemNameEn;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "image_url", length = 254)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Getters and Setters
}
