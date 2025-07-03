package git_kkalnane.starbucksbackenv2.domain.item.domain.dessert;

import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DessertItemService {
    private final DessertItemRepository dessertItemRepository;

    public DessertItemService(DessertItemRepository dessertItemRepository) {
        this.dessertItemRepository = dessertItemRepository;
    }

    public List<DessertItem> getAllDesserts() {
        return dessertItemRepository.findAll();
    }
}
