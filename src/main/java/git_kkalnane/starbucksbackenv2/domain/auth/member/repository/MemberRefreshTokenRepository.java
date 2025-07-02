package git_kkalnane.starbucksbackenv2.domain.auth.member.repository;

import git_kkalnane.starbucksbackenv2.domain.auth.member.domain.MemberRefreshToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRefreshTokenRepository extends JpaRepository<MemberRefreshToken, Long> {
  Optional<MemberRefreshToken> findByMemberId(Long memberId);

}
