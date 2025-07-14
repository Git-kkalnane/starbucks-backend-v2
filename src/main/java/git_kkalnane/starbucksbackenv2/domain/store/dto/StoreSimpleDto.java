package git_kkalnane.starbucksbackenv2.domain.store.dto;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreSimpleDto {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String openingHours;
    private Boolean hasDriveThrough;
    private Integer seatingCapacity;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String imageUrl;
    private String crowdLevel;
}

