package git_kkalnane.starbucksbackenv2.domain.item.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.List;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;

@Service
@RequiredArgsConstructor
public class ItemOptionService {
    private final ItemOptionRepository itemOptionRepository;
    
    public List<ItemOption> getItemOptionsByIds(List<Long> ids) {
        return itemOptionRepository.findAllById(ids);
    }
}
