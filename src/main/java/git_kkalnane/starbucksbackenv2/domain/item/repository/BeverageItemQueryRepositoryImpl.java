package git_kkalnane.starbucksbackenv2.domain.item.repository;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.QBeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.QBeverageSupportedSize;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BeverageItemQueryRepositoryImpl implements BeverageItemQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BeverageItem> findAllWithSupportedSizes(Pageable pageable) {
        QBeverageItem beverageItem = QBeverageItem.beverageItem;
        QBeverageSupportedSize supportedSize = QBeverageSupportedSize.beverageSupportedSize;

        // 동적 정렬 처리
        List<OrderSpecifier> orderSpecifiers = getOrderSpecifiers(pageable.getSort(), beverageItem);

        // 페이지네이션된 결과 조회
        List<BeverageItem> content = jpaQueryFactory.selectFrom(beverageItem).distinct()
                        .leftJoin(beverageItem.supportedSizes, supportedSize).fetchJoin()
                        .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
                        .offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();

        // 전체 카운트 조회
        Long total = jpaQueryFactory.select(beverageItem.count()).from(beverageItem).fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0L);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort, QBeverageItem beverageItem) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();
    @Override
    public Optional<BeverageItem> findByIdWithSupportedSizes(Long id) {
        QBeverageItem beverageItem = QBeverageItem.beverageItem;
        QBeverageSupportedSize supportedSize = QBeverageSupportedSize.beverageSupportedSize;

        BeverageItem result = jpaQueryFactory.selectFrom(beverageItem).distinct()
                        .leftJoin(beverageItem.supportedSizes, supportedSize).fetchJoin()
                        .where(beverageItem.id.eq(id)).fetchOne();

        return Optional.ofNullable(result);
    }


        if (sort.isEmpty()) {
            // 기본 정렬: id 오름차순
            orderSpecifiers.add(new OrderSpecifier<>(Order.ASC, beverageItem.id));
            return orderSpecifiers;
        }

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            OrderSpecifier<?> orderSpecifier = switch (property) {
                case "id" -> new OrderSpecifier<>(direction, beverageItem.id);
                case "itemNameKo" -> new OrderSpecifier<>(direction, beverageItem.itemNameKo);
                case "itemNameEn" -> new OrderSpecifier<>(direction, beverageItem.itemNameEn);
                case "price" -> new OrderSpecifier<>(direction, beverageItem.price);
                case "isCoffee" -> new OrderSpecifier<>(direction, beverageItem.isCoffee);
                case "description" -> new OrderSpecifier<>(direction, beverageItem.description);
                case "shotName" -> new OrderSpecifier<>(direction, beverageItem.shotName);
                case "supportedTemperatures" -> new OrderSpecifier<>(direction,
                                beverageItem.supportedTemperatures);
                default -> // 알 수 없는 필드의 경우 기본값으로 id 정렬
                        new OrderSpecifier<>(Order.ASC, beverageItem.id);
            };

            orderSpecifiers.add(orderSpecifier);
        }

        return orderSpecifiers;
    }
}
