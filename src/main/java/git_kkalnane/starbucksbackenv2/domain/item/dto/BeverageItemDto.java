package git_kkalnane.starbucksbackenv2.domain.item.dto;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSupportedSize;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class BeverageItemDto {

    private Long id;
    private String nameEn;
    private String nameKo;
    private String description;
    private Integer price;
    private Boolean isCoffee;
    private String hotImageUrl;
    private String iceImageUrl;
    private String shotName;
    private List<SupportedSizeDto> supportedSizes;
    private BeverageTemperatureOption supportedTemperatures;

    public BeverageItemDto(BeverageItem item) {
        this.id = item.getId();
        this.nameEn = item.getItemNameEn();
        this.nameKo = item.getItemNameKo();
        this.description = item.getDescription();
        this.price = item.getPrice();
        this.isCoffee = item.getIsCoffee();
        this.hotImageUrl = item.getHotImageUrl();
        this.iceImageUrl = item.getIceImageUrl();
        this.shotName = item.getShotName();
        this.supportedSizes = item.getSupportedSizes().stream().map(SupportedSizeDto::new).collect(Collectors.toList());
        this.supportedTemperatures = item.getSupportedTemperatures();
    }

    @Getter
    @NoArgsConstructor
    public static class SupportedSizeDto {
        private String name;
        private Integer price;
        private Integer volume;

        public SupportedSizeDto(BeverageSupportedSize size) {
            this.name = size.getName();
            this.price = size.getPrice();
            this.volume = size.getVolume();
        }
    }
}
