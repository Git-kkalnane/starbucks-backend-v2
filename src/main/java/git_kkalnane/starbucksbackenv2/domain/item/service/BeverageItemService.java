package git_kkalnane.starbucksbackenv2.domain.item.service;

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

    public BeveragePaginationResponse getAllBeverageItems(Pageable pageable) {

        Page<BeverageItem> paginationBeverages = beverageItemRepository.findAllWithSupportedSizes(pageable);

        List<BeverageItemDto> beverageItemDtos = paginationBeverages.map(BeverageItemDto::new).getContent();

        return BeveragePaginationResponse.builder().beverages(beverageItemDtos)
            .totalCount(paginationBeverages.getTotalElements())
            .currentPage(paginationBeverages.getNumber())
            .totalPages(paginationBeverages.getTotalPages())
            .pageSize(paginationBeverages.getSize()).build();
    }

    public List<BeverageItem> getBeverageByIds(List<Long> ids) {
        return beverageItemRepository.findAllById(ids);
    }
}
