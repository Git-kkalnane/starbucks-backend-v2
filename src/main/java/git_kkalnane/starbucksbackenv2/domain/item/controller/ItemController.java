package git_kkalnane.starbucksbackenv2.domain.item.controller;

import git_kkalnane.starbucksbackenv2.domain.item.common.success.ItemSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.item.dto.BeverageItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.DessertItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.ItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.BeveragePaginationResponse;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.DessertPaginationResponse;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.SingleBeverageResponse;
import git_kkalnane.starbucksbackenv2.domain.item.service.BeverageItemService;
import git_kkalnane.starbucksbackenv2.domain.item.service.DessertItemService;
import git_kkalnane.starbucksbackenv2.domain.item.service.ItemOptionService;
import git_kkalnane.starbucksbackenv2.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * 모든 음료 목록을 조회하는 컨트롤러 메서드
     *
     * @param pageable {@link Pageable} 객체
     * @return {@link BeveragePaginationResponse}를 포함한 응답
     */
    @Operation(summary = "전체 음료 목록 조회",
        description = "모든 음료 아이템 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "음료 목록 조회 성공")
    })
    @GetMapping("/drinks")
    public ResponseEntity<SuccessResponse<BeveragePaginationResponse>> getAllBeverages(
        @Parameter(hidden = true)
        @PageableDefault(size = 15, sort = "itemNameKo", direction = Direction.ASC)
        Pageable pageable
    ) {

        BeveragePaginationResponse response = beverageItemService.getAllBeverages(pageable);

        return ResponseEntity.ok(SuccessResponse.of(ItemSuccessCode.DRINKS_LIST_RETRIEVED, response));
    }

    /**
     * ID를 이용해 음료 상세 정보를 조회하는 컨트롤러 메서드
     *
     * @param id 조회할 음료의 ID
     * @return {@link SingleBeverageResponse}를 포함한 응답
     */
    @Operation(summary = "음료 상세 정보 조회",
        description = "음료 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "음료 상세 정보 조회 성공"),
        @ApiResponse(responseCode = "404", description = "해당 ID의 음료를 찾을 수 없음")
    })
    @GetMapping("/drinks/{id}")
    public ResponseEntity<SuccessResponse<SingleBeverageResponse>> getSpecificBeverage(@PathVariable Long id) {

        BeverageItemDto beverageItemDto = new BeverageItemDto(beverageItemService.getSpecificBeverage(id));
        List<ItemOptionDto> options = itemOptionService.getAvailableOptionsByItemId(id);

        SingleBeverageResponse response = new SingleBeverageResponse(beverageItemDto, options);

        return ResponseEntity.ok(SuccessResponse.of(ItemSuccessCode.DRINK_DETAIL_RETRIEVED, response));
    }

    /**
     * 모든 디저트 목록을 조회하는 컨트롤러 메서드
     * @param pageable {@link Pageable} 객체
     * @return {@link DessertPaginationResponse}를 포함한 응답
     */
    @Operation(summary = "전체 디저트 목록 조회",
        description = "모든 디저트 아이템 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "디저트 목록 조회 성공")
    })
    @GetMapping("/desserts")
    public ResponseEntity<SuccessResponse<DessertPaginationResponse>> getAllDesserts(
        @Parameter(hidden = true)
        @PageableDefault(size = 15, sort = "dessertItemNameKo", direction = Direction.ASC)
        Pageable pageable
    ) {
        DessertPaginationResponse response = dessertItemService.getAllDesserts(pageable);

        return ResponseEntity.ok(SuccessResponse.of(ItemSuccessCode.DESSERT_LIST_RETRIEVED, response));
    }

    /**
     * ID를 이용해 디저트 상세 정보를 조회하는 메서드
     * @param id 조회할 디저트의 ID
     * @return {@link DessertItemDto}를 포함한 응답
     */
    @Operation(summary = "디저트 상세 정보 조회",
        description = "디저트 ID로 상세 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "디저트 상세 정보 조회 성공"),
        @ApiResponse(responseCode = "404", description = "해당 ID의 디저트를 찾을 수 없음")
    })
    @GetMapping("/desserts/{id}")
    public ResponseEntity<SuccessResponse<DessertItemDto>> getSpecificDessert(@PathVariable Long id) {

        DessertItemDto response = dessertItemService.getSpecificDessert(id);

        return ResponseEntity.ok(SuccessResponse.of(ItemSuccessCode.DESSERT_DETAIL_RETRIEVED, response));
    }
}
