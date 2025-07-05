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

    /**
     * 포인트 입금(증가)
     */
    public void addPointAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("입금 금액은 0보다 커야 합니다.");
        }
        this.pointAmount += amount;
    }

    /**
     * 포인트 출금(감소)
     */
    public void subtractPointAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("출금 금액은 0보다 커야 합니다.");
        }
        if (this.pointAmount < amount) {
            throw new IllegalArgumentException("포인트 잔액이 부족합니다.");
        }
        this.pointAmount -= amount;
    }

}
