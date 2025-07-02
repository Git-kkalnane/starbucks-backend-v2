package git_kkalnane.starbucksbackenv2.domain.merchant.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "merchant")
public class Merchant extends git_kkalnane.starbucksbackenv2.global.entity.BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", length = 50, unique = true, nullable = false)
    private String merchantName;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String passwordHash;

    // Getters and Setters
}
