package git_kkalnane.starbucksbackenv2.domain.item.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.QItemOption;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ItemOptionQueryRepositoryImpl implements ItemOptionQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ItemOption> findAllByItemId(Long itemId) {
        QItemOption itemOption = QItemOption.itemOption;

        return jpaQueryFactory.selectFrom(itemOption).distinct().where(itemOption.itemId.eq(itemId))
                        .fetch();
    }

    @Override
    public String findNameById(Long id) {
        QItemOption itemOption = QItemOption.itemOption;

        return jpaQueryFactory.select(itemOption.name).from(itemOption).where(itemOption.id.eq(id))
                        .fetchOne();
    }
}
