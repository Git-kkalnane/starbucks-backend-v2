package git_kkalnane.starbucksbackenv2.domain.inqurey.repository;

import git_kkalnane.starbucksbackenv2.domain.inqurey.domain.Inquiry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InquiryRepository extends JpaRepository<Inquiry, Long> {
}
