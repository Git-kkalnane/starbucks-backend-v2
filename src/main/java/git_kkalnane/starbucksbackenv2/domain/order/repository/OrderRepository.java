package git_kkalnane.starbucksbackenv2.domain.order.repository;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

    @Query("SELECT new git_kkalnane.starbucksbackenv2.domain.order.repository.OrderRepository$OrderDetailDto(" +
            "o.id, o.orderNumber, o.status, o.createdAt, o.pickupType, o.totalPrice, " +
            "s.id, s.name, " +
            "m.id, m.nickname, " +
            "oi.id, " +
            "CASE WHEN oi.itemType = 'BEVERAGE' OR oi.itemType = 'COFFEE' THEN oi.beverageItemId ELSE oi.dessertItemId END, " +
            "oi.itemName, oi.finalItemPrice, oi.quantity, " +
            "opt.name" +
            ") " +
            "FROM Order o " +
            "JOIN o.member m " +
            "JOIN o.store s " +
            "JOIN o.orderItems oi " +
            "LEFT JOIN oi.orderItemOptions oio " +
            "LEFT JOIN oio.itemOption opt " +
            "WHERE o.id = :orderId")
    List<OrderDetailDto> findOrderDetailDtoById(@Param("orderId") Long orderId);

    record OrderDetailDto(
            Long orderId, String orderNumber, OrderStatus status, LocalDateTime orderDateTime, PickupType pickupType, Long totalPrice,
            Long storeId, String storeName,
            Long memberId, String memberNickname,
            Long orderItemId, Long itemId, String itemName, Long finalPrice, Long quantity,
            String optionName
    ) {}
}