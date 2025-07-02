package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "order_item_options")
public class OrderItemOption {
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
