package git_kkalnane.starbucksbackenv2.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "access_token")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, length = 255)
    private String memberId;

    @Column(nullable = false, length = 255)
    private String token;

    @Column
    private LocalDate expiration;

    // Getters and Setters
}
