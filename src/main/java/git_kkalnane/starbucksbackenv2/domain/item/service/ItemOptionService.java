package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.dto.ItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemOptionService {

    private final ItemOptionRepository itemOptionRepository;

    /**
     * 아이템 ID로 사용 가능한 옵션 목록을 조회합니다.<br>
     * <br>
     * 캐시 적용: 아이템별 옵션 정보는 자주 조회되므로 캐싱하여 성능 향상
     *
     * @param itemId 조회할 아이템 ID
     * @return 사용 가능한 옵션 목록
     */
    @Cacheable(value = "itemOptions", cacheNames = "itemOptions", key = "#itemId")
    public List<ItemOptionDto> getAvailableOptionsByItemId(Long itemId) {

        return itemOptionRepository.findAllByItemId(itemId).stream()
                        .map(option -> new ItemOptionDto(option.getName(), option.getOptionPrice()))
                        .toList();
    }

    /**
     * 음료의 ID 값들을 가지고 있는 리스트를 인수로 받아 ItemOption 객체 리스트를 반환하는 메서드 (OrderService에서 사용)<br>
     * <br>
     * 캐시 적용: ID 리스트를 키로 사용하여 동일한 ID 조합 요청 시 캐시된 결과 반환
     *
     * @param ids 음료의 ID 값들을 가지고 있는 List
     * @return {@link ItemOption} 객체 리스트
     */
    @Cacheable(value = "itemOptionsByIds", cacheNames = "itemOptionsByIds", key = "#ids.toString()")
    public List<ItemOption> getItemOptionsByIds(List<Long> ids) {
        return itemOptionRepository.findAllById(ids);
    }

}
