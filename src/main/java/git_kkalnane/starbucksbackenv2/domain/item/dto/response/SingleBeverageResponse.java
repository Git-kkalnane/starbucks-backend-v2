package git_kkalnane.starbucksbackenv2.domain.item.dto.response;

import git_kkalnane.starbucksbackenv2.domain.item.dto.BeverageItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.ItemOptionDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SingleBeverageResponse {

    private BeverageItemDto beverageInfo;
    private List<ItemOptionDto> options;
}
