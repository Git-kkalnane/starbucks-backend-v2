package git_kkalnane.starbucksbackenv2.domain.auth.merchant.repository;

import git_kkalnane.starbucksbackenv2.domain.auth.merchant.domain.MerchantRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRefreshTokenRepository extends JpaRepository<MerchantRefreshToken, Long> {
    Optional<MerchantRefreshToken> findByMemberId(Long memberId);
}
