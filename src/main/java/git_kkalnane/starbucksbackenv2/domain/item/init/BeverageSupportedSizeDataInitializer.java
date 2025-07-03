package git_kkalnane.starbucksbackenv2.domain.item.init;

import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSizeOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageSupportedSize;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageSupportedSizeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class BeverageSupportedSizeDataInitializer {

  private final BeverageSupportedSizeRepository beverageSupportedSizeRepository;

  @PostConstruct
  public void init() {
    if (beverageSupportedSizeRepository.count() == 0) {
      beverageSupportedSizeRepository.saveAll(Arrays.asList(
          BeverageSupportedSize.builder().name(BeverageSizeOption.TALL.name()).price(200).volume(355).build(),
          BeverageSupportedSize.builder().name(BeverageSizeOption.GRANDE.name()).price(300).volume(473).build(),
          BeverageSupportedSize.builder().name(BeverageSizeOption.VENTI.name()).price(400).volume(591).build(),
          BeverageSupportedSize.builder().name(BeverageSizeOption.SHORT.name()).price(100).volume(237).build(),
          BeverageSupportedSize.builder().name(BeverageSizeOption.SOLO.name()).price(100).volume(30).build(),
          BeverageSupportedSize.builder().name(BeverageSizeOption.DOPPIO.name()).price(100).volume(60).build()
          ));
    }
  }
}
