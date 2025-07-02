package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart")
    private java.util.List<CartItem> cartItems;

    // Getters and Setters
}
