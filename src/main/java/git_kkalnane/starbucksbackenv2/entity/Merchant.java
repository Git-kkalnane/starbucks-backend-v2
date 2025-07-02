package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchantname", length = 50, unique = true, nullable = false)
    private String merchantName;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(name = "password_hash", length = 255, nullable = false)
    private String passwordHash;

    // Getters and Setters
}
