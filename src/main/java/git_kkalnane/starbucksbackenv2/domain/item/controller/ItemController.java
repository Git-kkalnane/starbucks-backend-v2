package git_kkalnane.starbucksbackenv2.domain.item.controller;

import git_kkalnane.starbucksbackenv2.domain.item.common.success.ItemSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.dto.BeverageItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.ItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.BeveragePaginationResponse;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.SingleBeverageResponse;
import git_kkalnane.starbucksbackenv2.domain.item.service.BeverageItemService;
import git_kkalnane.starbucksbackenv2.domain.item.service.DessertItemService;
import git_kkalnane.starbucksbackenv2.domain.item.service.ItemOptionService;
import git_kkalnane.starbucksbackenv2.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final BeverageItemService beverageItemService;
    private final DessertItemService dessertItemService;
    private final ItemOptionService itemOptionService;

    @GetMapping("/drinks")
    public ResponseEntity<SuccessResponse<BeveragePaginationResponse>> getAllBeverages(
        @Parameter(hidden = true)
        @PageableDefault(size = 15, sort = "itemNameKo", direction = Direction.ASC)
        Pageable pageable) {

        BeveragePaginationResponse response = beverageItemService.getAllBeverageItems(pageable);

        return ResponseEntity.ok(SuccessResponse.of(ItemSuccessCode.DRINKS_LIST_RETRIEVED, response));
    }

    @GetMapping("/drinks/{id}")
    public ResponseEntity<SuccessResponse<?>> getSpecificBeverage(@PathVariable Long id) {

        BeverageItemDto beverageItemDto = new BeverageItemDto(beverageItemService.getSpecificBeverage(id));
        List<ItemOptionDto> options = itemOptionService.getAvailableOptionsByItemId(id);

        SingleBeverageResponse response = new SingleBeverageResponse(beverageItemDto, options);

        return ResponseEntity.ok(SuccessResponse.of(ItemSuccessCode.DRINK_DETAIL_RETRIEVED, response));
    }

    @GetMapping("/desserts")
    public List<DessertItem> getAllDesserts() {
        return dessertItemService.getAllDesserts();
    }
}
