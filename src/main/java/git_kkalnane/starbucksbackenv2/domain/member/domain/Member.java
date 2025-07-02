package git_kkalnane.starbucksbackenv2.domain.member.domain;

import git_kkalnane.starbucksbackenv2.domain.inqurey.domain.Inquiry;
import git_kkalnane.starbucksbackenv2.domain.cart.domain.Cart;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.Notification;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Getter
@ToString
@Table(name = "members")
public class Member extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_name", length = 50)
    private String name;

    @Column(length = 50)
    private String nickname;

    @Column(length = 255, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String password;

        @OneToMany(mappedBy = "member")
    private java.util.List<Inquiry> inquiries;

    @OneToMany(mappedBy = "member")
    private java.util.List<Cart> carts;

    @OneToMany(mappedBy = "member")
    private java.util.List<PayCard> payCards;

    @OneToMany(mappedBy = "member")
    private java.util.List<PointCard> pointCards;

    @OneToMany(mappedBy = "member")
    private java.util.List<Order> orders;

    @OneToMany(mappedBy = "member")
    private java.util.List<Notification> notifications;

}
