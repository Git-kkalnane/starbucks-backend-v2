package git_kkalnane.starbucksbackenv2.domain.store.service;

import git_kkalnane.starbucksbackenv2.domain.store.common.exception.StoreErrorCode;
import git_kkalnane.starbucksbackenv2.domain.store.common.exception.StoreException;
import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.store.repository.StoreRepository;
import git_kkalnane.starbucksbackenv2.domain.store.dto.response.StoreListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {
    private final StoreRepository storeRepository;


    /**
     * 특정 매장의 상세 정보를 조회합니다.
     *
     * @param storeId 조회할 매장의 ID
     * @return 매장의 상세 정보 DTO
     * @throws StoreException 매장을 찾지 못한 경우
     */
    public StoreSimpleDto getStoreDetails(Long storeId) {
        StoreSimpleDto store = storeRepository.findSimpleDtoById(storeId)
            .orElseThrow(() -> new StoreException(StoreErrorCode.STORE_NOT_FOUND));
        return store;
    }
    /**
     * 페이징 처리된 전체 지점 목록을 조회합니다.
     */
    public StoreListResponse getStoreList(Pageable pageable) {
        Page<StoreSimpleDto> storePage = storeRepository.findAllSimpleStores(pageable);
        return StoreListResponse.builder()
            .stores(storePage.getContent())
            .totalCount(storePage.getTotalElements())
            .currentPage(storePage.getNumber())
            .totalPages(storePage.getTotalPages())
            .pageSize(storePage.getSize())
            .build();
    }
}
