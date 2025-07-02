package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @OneToMany(mappedBy = "order")
    private java.util.List<OrderItem> orderItems;

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

    // Getters and Setters
}
