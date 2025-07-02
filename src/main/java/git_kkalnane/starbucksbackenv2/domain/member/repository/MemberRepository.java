package git_kkalnane.starbucksbackenv2.domain.member.repository;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findMemberByEmail(String email);

  boolean existsByEmail(String email);

}
