package git_kkalnane.starbucksbackenv2.domain.auth.merchant.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "merchant_refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class MerchantRefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "merchant_id", nullable = false)
    private Long memberId;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "expiration", nullable = false)
    private Date expiration;

    public void modifyToken(String token) {
        this.token = token;
    }

    public void modifyExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean validateToken(String refreshToken) {
        return Objects.equals(this.token, refreshToken);
    }
}
