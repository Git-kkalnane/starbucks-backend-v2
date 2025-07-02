package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_favorit_items")
public class CartFavoritItem {
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
