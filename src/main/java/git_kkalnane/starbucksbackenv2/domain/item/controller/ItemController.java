package git_kkalnane.starbucksbackenv2.domain.item.controller;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.dto.BeverageItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.service.BeverageItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("items")
public class ItemController {
    private final BeverageItemService beverageItemService;

    @GetMapping("drinks")
    public List<BeverageItemDto> getAllBeverages() {
        return beverageItemService.getAllBeverageItems()
                .stream()
                .map(BeverageItemDto::new)
                .collect(java.util.stream.Collectors.toList());
    }
}
