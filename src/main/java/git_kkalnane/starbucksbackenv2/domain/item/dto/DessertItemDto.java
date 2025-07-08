package git_kkalnane.starbucksbackenv2.domain.item.dto;

import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class DessertItemDto {

    private Long id;
    private String dessertItemNameKo;
    private String dessertItemNameEn;
    private String description;
    private Integer price;
    private String imageUrl;

    public DessertItemDto(DessertItem item) {
        this.id = item.getId();
        this.dessertItemNameKo = item.getDessertItemNameKo();
        this.dessertItemNameEn = item.getDessertItemNameEn();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.imageUrl = item.getImageUrl();
    }
}
