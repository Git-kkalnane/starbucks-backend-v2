package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.common.exception.ItemErrorCode;
import git_kkalnane.starbucksbackenv2.domain.item.common.exception.ItemException;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.dto.DessertItemDto;
import git_kkalnane.starbucksbackenv2.domain.item.dto.response.DessertPaginationResponse;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DessertItemService {

    private final DessertItemRepository dessertItemRepository;

    /**
     * 모든 디저트를 조회, 정렬, 페이징하여 반환합니다.<br>
     * <br>
     * 캐시 적용: 페이징 정보를 키로 사용하여 동일한 페이지 요청 시 캐시된 결과 반환
     *
     * @param pageable 페이징 및 정렬 정보
     * @return {@link DessertPaginationResponse} 객체
     */
    @Cacheable(value = "dessertList", cacheNames = "dessertList",
                    key = "#pageable.pageNumber + '_' + #pageable.pageSize + '_' + #pageable.sort.toString()")
    public DessertPaginationResponse getAllDesserts(Pageable pageable) {
        Page<DessertItem> paginationDesserts = dessertItemRepository.findAll(pageable);

        List<DessertItemDto> dessertItemDtos =
                        paginationDesserts.map(DessertItemDto::new).getContent();

        return DessertPaginationResponse.builder().desserts(dessertItemDtos)
                        .totalCount(paginationDesserts.getTotalElements())
                        .currentPage(paginationDesserts.getNumber())
                        .totalPages(paginationDesserts.getTotalPages())
                        .pageSize(paginationDesserts.getSize()).build();
    }

    /**
     * ID로 디저트 상세 정보를 조회합니다.<br>
     * <br>
     * 캐시 적용: 개별 디저트 정보는 자주 조회되므로 캐싱하여 성능 향상
     *
     * @param id 조회할 음료 ID
     * @return {@link DessertItemDto} 객체
     * @throws ItemException 해당 ID를 가진 디저트를 DB에서 찾을 수 없는 경우
     */
    @Cacheable(value = "dessertItems", cacheNames = "dessertItems", key = "#id")
    public DessertItemDto getSpecificDessert(Long id) {
        DessertItem dessertItem = dessertItemRepository.findById(id)
                        .orElseThrow(() -> new ItemException(ItemErrorCode.DESSERT_NOT_FOUND));

        return new DessertItemDto(dessertItem);
    }

    /**
     * 디저트의 ID 값들을 가지고 있는 리스트를 인수로 받아 DessertItem 객체 리스트를 반환하는 메서드 (OrderService에서 사용)<br>
     * <br>
     * 캐시 적용: ID 리스트를 키로 사용하여 동일한 ID 조합 요청 시 캐시된 결과 반환
     *
     * @param ids 디저트의 ID 값들을 가지고 있는 List
     * @return {@link DessertItem} 객체 리스트
     */
    @Cacheable(value = "dessertsByIds", cacheNames = "dessertsByIds", key = "#ids.toString()")
    public List<DessertItem> getDessertsByIds(List<Long> ids) {
        return dessertItemRepository.findAllById(ids);
    }
}
