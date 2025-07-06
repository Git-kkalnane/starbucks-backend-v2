package git_kkalnane.starbucksbackenv2.domain.order.repository;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * [고객용] 특정 회원의 특정 주문 상태 목록에 해당하는 모든 주문을 조회합니다. (생성일시 오름차순)
     */
    List<Order> findByMemberIdAndStatusInOrderByCreatedAtAsc(Long memberId, List<OrderStatus> statuses);

    /**
     * [고객용] 특정 회원의 과거 주문 내역(완료/취소)을 페이지네이션하여 조회합니다.
     */
    Page<Order> findByMemberIdAndStatusIn(Long memberId, Collection<OrderStatus> statuses, Pageable pageable);

    /**
     * [매장용] 특정 매장의 특정 주문 상태 목록에 해당하는 모든 주문을 조회합니다. (생성일시 오름차순)
     */
    List<Order> findByStoreIdAndStatusInOrderByCreatedAtAsc(Long storeId, List<OrderStatus> statuses);

    /**
     * [매장용] 특정 매장의 과거 주문 내역(완료/취소)을 페이지네이션하여 조회합니다.
     */
    Page<Order> findByStoreIdAndStatusIn(Long storeId, Collection<OrderStatus> statuses, Pageable pageable);
}