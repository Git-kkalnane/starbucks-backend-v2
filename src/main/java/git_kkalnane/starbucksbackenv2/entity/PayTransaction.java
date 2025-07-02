package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pay_transaction")
public class PayTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "paycard_id", nullable = false)
    private PayCard payCard;

    @Column(name = "pay_amount")
    private Long payAmount;

    @Column
    private String description;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    // Getters and Setters
}
