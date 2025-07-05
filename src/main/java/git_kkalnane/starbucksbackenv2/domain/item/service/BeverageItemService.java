package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BeverageItemService {
    private final BeverageItemRepository beverageItemRepository;

    public List<BeverageItem> getAllBeverageItems() {
        return beverageItemRepository.findAllWithSupportedSizes();
    }

    public List<BeverageItem> getBeverageByIds(List<Long> ids) {
        return beverageItemRepository.findAllById(ids);
    }
}
