package git_kkalnane.starbucksbackenv2.domain.pointcard.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity;
import git_kkalnane.starbucksbackenv2.domain.paycard.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "point_transaction")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointTransaction extends BaseTimeEntity {

    @Builder
    public PointTransaction(PointCard pointCard, Long payAmount, String description, TransactionType type) {
        this.pointCard = pointCard;
        this.payAmount = payAmount;
        this.description = description;
        this.type = type;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "point_card_id", nullable = false)
    private PointCard pointCard;

    @Column(name = "pay_amount")
    private Long payAmount;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType type;


}
