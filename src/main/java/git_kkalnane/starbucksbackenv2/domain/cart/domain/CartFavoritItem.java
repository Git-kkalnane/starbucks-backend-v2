package git_kkalnane.starbucksbackenv2.domain.cart.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_favorit_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CartFavoritItem extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "cartFavoritItem")
    private java.util.List<CartItem> cartItems;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Getters and Setters
}
