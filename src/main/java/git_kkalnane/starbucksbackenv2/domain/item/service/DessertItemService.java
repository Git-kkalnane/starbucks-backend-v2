package git_kkalnane.starbucksbackenv2.domain.item.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DessertItemService {
    private final DessertItemRepository dessertItemRepository;

    public List<DessertItem> getAllDesserts() {
        return dessertItemRepository.findAll();
    }
    
    public List<DessertItem> getDessertsByIds(List<Long> ids) {
        return dessertItemRepository.findAllById(ids);
    }
}
