package git_kkalnane.starbucksbackenv2.domain.store.controller;

import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("stores")
@RequiredArgsConstructor
public class StoreController {
    private final StoreService storeService;

    @GetMapping
    public List<StoreSimpleDto> getStoreList() {
        return storeService.getStoreList();
    }
}
