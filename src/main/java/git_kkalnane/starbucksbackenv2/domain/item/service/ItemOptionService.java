package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.dto.ItemOptionDto;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemOptionService {

    private final ItemOptionRepository itemOptionRepository;

    public List<ItemOptionDto> getAvailableOptionsByItemId(Long itemId) {

        return itemOptionRepository.findAllByItemId(itemId).stream()
            .map(option -> new ItemOptionDto(option.getName(), option.getOptionPrice())).toList();
    }

    /**
     * 음료의 ID 값들을 가지고 있는 리스트를 인수로 받아 ItemOption 객체 리스트를 반환하는 메서드 (OrderService에서 사용)
     *
     * @param ids 음료의 ID 값들을 가지고 있는 List
     * @return {@link ItemOption} 객체 리스트
     */
    public List<ItemOption> getItemOptionsByIds(List<Long> ids) {
        return itemOptionRepository.findAllById(ids);
    }

}
