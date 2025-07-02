package git_kkalnane.starbucksbackenv2.domain.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item_options")
public class OrderItemOption extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_item_id")
    private OrderItem orderItem;

    @Column(name = "item_option_id")
    private Long itemOptionId;

    @Column
    private Long quantity;

    // Getters and Setters
}
