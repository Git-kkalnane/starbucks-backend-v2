package git_kkalnane.starbucksbackenv2.domain.pointcard.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayCard;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "pointcard")
@NoArgsConstructor
@Getter
@ToString
public class PointCard extends BaseTimeEntity {

    @Builder
    public PointCard(Long id, Member member, List<PointTransaction> transactions, String cardNumber, Integer pointAmount) {
        this.id = id;
        this.member = member;
        this.pointTransactions = transactions;
        this.cardNumber = cardNumber;
        this.pointAmount = pointAmount;
    }

    @Builder
    public PointCard(PointCard pointCard) {
        this.id = pointCard.id;
        this.member = pointCard.member;
        this.pointTransactions = pointCard.pointTransactions;
        this.cardNumber = pointCard.cardNumber;
        this.pointAmount = pointCard.pointAmount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "pointCard")
    private List<PointTransaction> pointTransactions;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "point_amount", nullable = false)
    private Integer pointAmount;

}
