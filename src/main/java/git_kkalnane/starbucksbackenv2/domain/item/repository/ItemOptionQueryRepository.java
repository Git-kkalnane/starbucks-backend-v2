package git_kkalnane.starbucksbackenv2.domain.item.repository;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import java.util.List;

public interface ItemOptionQueryRepository {

    List<ItemOption> findAllByItemId(Long itemId);

    String findNameById(Long id);
}
