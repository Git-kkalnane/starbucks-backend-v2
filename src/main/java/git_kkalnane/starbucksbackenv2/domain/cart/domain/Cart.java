package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "carts")
public class Cart extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cart")
    private java.util.List<CartItem> cartItems;

    // Getters and Setters
}
