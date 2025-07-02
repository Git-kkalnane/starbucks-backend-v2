package git_kkalnane.starbucksbackenv2.domain.pointcard.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import jakarta.persistence.*;

@Entity
@Table(name = "pointcard")
public class PointCard extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "pointCard")
    private java.util.List<PointTransaction> pointTransactions;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "point_amount", nullable = false)
    private Long pointAmount;

    // Getters and Setters
}
