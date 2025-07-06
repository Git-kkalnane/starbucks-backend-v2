package git_kkalnane.starbucksbackenv2.domain.merchant.repository;

import java.util.Optional;

import git_kkalnane.starbucksbackenv2.domain.merchant.domain.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {
    Optional<Merchant> findByEmail(String email);

    boolean existsByEmail(String email);
}
