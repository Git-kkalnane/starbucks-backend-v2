package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.dto.DessertItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.DessertPaginationResponse;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DessertItemService {

    private final DessertItemRepository dessertItemRepository;

    /**
     * 모든 디저트를 조회, 정렬, 페이징하여 반환합니다.
     *
     * @param pageable 페이징 및 정렬 정보
     * @return {@link DessertPaginationResponse} 객체
     */
    public DessertPaginationResponse getAllDesserts(Pageable pageable) {
        Page<DessertItem> paginationDesserts = dessertItemRepository.findAll(pageable);

        List<DessertItemDto> dessertItemDtos = paginationDesserts.map(DessertItemDto::new).getContent();

        return DessertPaginationResponse.builder().desserts(dessertItemDtos)
            .totalCount(paginationDesserts.getTotalElements())
            .currentPage(paginationDesserts.getNumber())
            .totalPages(paginationDesserts.getTotalPages())
            .pageSize(paginationDesserts.getSize()).build();
    }

    /**
     * 디저트의 ID 값들을 가지고 있는 리스트를 인수로 받아 DessertItem 객체 리스트를 반환하는 메서드 (OrderService에서 사용)
     *
     * @param ids 디저트의 ID 값들을 가지고 있는 List
     * @return {@link DessertItem} 객체 리스트
     */
    public List<DessertItem> getDessertsByIds(List<Long> ids) {
        return dessertItemRepository.findAllById(ids);
    }
}
