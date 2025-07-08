package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.common.exception.ItemErrorCode;
import git_kkalnane.starbucksbackenv2.domain.item.common.exception.ItemException;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.dto.BeverageItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.BeveragePaginationResponse;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BeverageItemService {

    private final BeverageItemRepository beverageItemRepository;

    /**
     * 모든 음료(커피 포함) 목록을 조회, 정렬, 페이징하여 반환합니다.
     *
     * @param pageable 페이징 및 정렬 정보
     * @return {@link BeveragePaginationResponse}
     */
    public BeveragePaginationResponse getAllBeverages(Pageable pageable) {
        Page<BeverageItem> paginationBeverages = beverageItemRepository.findAllWithSupportedSizes(pageable);

        List<BeverageItemDto> beverageItemDtos = paginationBeverages.map(BeverageItemDto::new).getContent();

        return BeveragePaginationResponse.builder().beverages(beverageItemDtos)
            .totalCount(paginationBeverages.getTotalElements())
            .currentPage(paginationBeverages.getNumber())
            .totalPages(paginationBeverages.getTotalPages())
            .pageSize(paginationBeverages.getSize()).build();
    }

    /**
     * ID로 음료 상세 정보를 조회합니다.
     *
     * @param id 조회할 음료 ID
     * @return {@link BeverageItem} 객체
     * @throws ItemException 해당 ID를 가진 음료를 DB에서 찾을 수 없는 경우
     */
    public BeverageItem getSpecificBeverage(Long id) {
        return beverageItemRepository.findByIdWithSupportedSizes(id)
            .orElseThrow(() -> new ItemException(ItemErrorCode.BEVERAGE_NOT_FOUND));
    }

    /**
     * 음료의 ID 값들을 가지고 있는 리스트를 인수로 받아 BeverageItem 객체 리스트를 반환하는 메서드 (OrderService에서 사용)
     *
     * @param ids 음료의 ID 값들을 가지고 있는 List
     * @return {@link BeverageItem} 객체 리스트
     */
    public List<BeverageItem> getBeverageByIds(List<Long> ids) {
        return beverageItemRepository.findAllById(ids);
    }
}
