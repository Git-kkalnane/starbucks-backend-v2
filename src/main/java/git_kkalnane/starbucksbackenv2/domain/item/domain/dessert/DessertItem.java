package git_kkalnane.starbucksbackenv2.domain.item.domain.dessert;

import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "dessert_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DessertItem extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name_ko", length = 50, unique = true, nullable = false)
    private String dessertItemNameKo;

    @Column(name = "name_en", length = 50, unique = true, nullable = false)
    private String dessertItemNameEn;

    @Column(nullable = false, columnDefinition = "text")
    private String description;

    @Column(nullable = false)
    private Integer price;

    @Column(name = "image_url", length = 254)
    private String imageUrl;



    // Getters and Setters
}
