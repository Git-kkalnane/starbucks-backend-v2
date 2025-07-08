package git_kkalnane.starbucksbackenv2.domain.member.domain;

import git_kkalnane.starbucksbackenv2.domain.inqurey.domain.Inquiry;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.Notification;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import jakarta.persistence.*;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@Table(name = "members")
public class Member extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {

    @Builder
    Member(Long id, String name, String nickname, String email, String password,
        List<Inquiry> inquiries, List<Cart> carts, List<PayCard> payCards,
        PointCard pointCards, List<Order> orders) {

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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50)
    private String name;

    @Column(length = 50)
    private String nickname;

    @Column(length = 255, unique = true)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @OneToMany(mappedBy = "member")
    private java.util.List<Inquiry> inquiries;

    @OneToMany(mappedBy = "member")
    private java.util.List<Cart> carts;

    @OneToMany(mappedBy = "member")
    private java.util.List<PayCard> payCards;

    @OneToOne(mappedBy = "member")
    private PointCard pointCard;

    @OneToMany(mappedBy = "member")
    private java.util.List<Order> orders;

}
