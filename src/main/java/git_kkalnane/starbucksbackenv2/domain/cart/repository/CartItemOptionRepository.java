package git_kkalnane.starbucksbackenv2.domain.cart.repository;

import git_kkalnane.starbucksbackenv2.domain.cart.domain.CartItemOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemOptionRepository extends JpaRepository<CartItemOption, Long> {
    List<CartItemOption> findAllByCartItemId(Long cartItemId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM CartItemOption cio WHERE cio.cartItem.id IN :cartItemIds")
    void deleteAllByCartItemIds(@Param("cartItemIds") List<Long> cartItemIds);
}
