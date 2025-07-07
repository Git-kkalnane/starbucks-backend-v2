package git_kkalnane.starbucksbackenv2.domain.order.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderCreateRequest;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @Column(name = "order_number", length = 20, unique = true, nullable = false)
    private String orderNumber;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PLACED;

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_type", nullable = false)
    private PickupType pickupType;

    @Column(name = "request_memo", columnDefinition = "text")
    private String requestMemo;

    @Column(name = "expected_pickup_time")
    private LocalDateTime expectedPickupTime;

    @Column(name = "card_number")
    private String cardNumber;

    /**
     * Order 엔티티를 생성하는 정적 팩토리 메서드.
     * OrderItem과의 연관관계를 설정하는 로직을 포함합니다.
     */
    public static Order createOrderEntity(Member member, Store store, String orderNumber, Long totalPrice, OrderCreateRequest request, List<OrderItem> orderItems) {
        Order order = Order.builder()
                .member(member)
                .store(store)
                .orderNumber(orderNumber)
                .totalPrice(totalPrice)
                .status(OrderStatus.PLACED)
                .pickupType(request.pickupType())
                .requestMemo(request.requestMemo())
                .expectedPickupTime(LocalDateTime.now().plusMinutes(10))
                .cardNumber(request.cardNumber())
                .orderItems(new ArrayList<>())
                .build();

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }

        return order;
    }

    /**
     * 주문 상태를 변경하는 비즈니스 메서드
     */
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
    }

    /**
     * 연관관계 편의 메서드.
     * Order에 OrderItem을 추가할 때, OrderItem에도 Order를 설정하여 양방향 관계를 동기화합니다.
     */
    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
    }
}
