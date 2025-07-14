package git_kkalnane.starbucksbackenv2.domain.member.domain;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.inqurey.domain.Inquiry;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(length = 50)
    private String nickname;

    @Column(unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    @OneToMany(mappedBy = "member")
    private List<Inquiry> inquiries;

    @OneToMany(mappedBy = "member")
    private List<Cart> carts;

    @OneToMany(mappedBy = "member")
    private List<PayCard> payCards;

    @OneToOne(mappedBy = "member")
    private PointCard pointCard;

    @OneToMany(mappedBy = "member")
    private List<Order> orders;

    @Builder
    Member(Long id, String name, String nickname, String email, String password,
           List<Inquiry> inquiries, List<Cart> carts, List<PayCard> payCards,
           PointCard pointCard, List<Order> orders) {

        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.inquiries = inquiries;
        this.carts = carts;
        this.payCards = payCards;
        this.pointCard = pointCard;
        this.orders = orders;
    }

    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }

    public void modifyPassword(String password) {
        this.password = password;
    }

}
