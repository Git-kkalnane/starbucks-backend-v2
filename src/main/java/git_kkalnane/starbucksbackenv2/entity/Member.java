package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "members")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_name", length = 50)
    private String memberName;

    @Column(length = 50)
    private String nickname;

    @Column(length = 255, unique = true)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

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

    // Getters and Setters
}
