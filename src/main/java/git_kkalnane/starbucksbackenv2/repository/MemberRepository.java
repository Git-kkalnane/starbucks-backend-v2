package git_kkalnane.starbucksbackenv2.repository;

import git_kkalnane.starbucksbackenv2.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
