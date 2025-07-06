package git_kkalnane.starbucksbackenv2.domain.paycard.domain;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "paycard",
    uniqueConstraints = @UniqueConstraint(columnNames = "member_id")
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PayCard extends BaseTimeEntity {
    @Builder
    PayCard(Long id, Member member, java.util.List<PayTransaction> payTransactions, String cardNumber, int cardAmount) {
        this.id = id;
        this.member = member;
        this.payTransactions = payTransactions;
        this.cardNumber = cardNumber;
        this.cardAmount = cardAmount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "payCard")
    private java.util.List<PayTransaction> payTransactions;

    @Column(name = "card_number", unique = true, nullable = false)
    private String cardNumber;

    @Column(name = "card_amount", nullable = false)
    private int cardAmount;

}
