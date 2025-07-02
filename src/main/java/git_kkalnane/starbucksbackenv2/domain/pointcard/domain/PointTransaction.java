package git_kkalnane.starbucksbackenv2.domain.pointcard.domain;

import git_kkalnane.starbucksbackenv2.domain.paycard.TransactionType;
import jakarta.persistence.*;

@Entity
@Table(name = "point_transaction")
public class PointTransaction extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
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

    // Getters and Setters
}
