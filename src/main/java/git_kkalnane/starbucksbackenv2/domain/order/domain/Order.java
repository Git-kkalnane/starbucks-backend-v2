package git_kkalnane.starbucksbackenv2.domain.order.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus; 
    }
}
