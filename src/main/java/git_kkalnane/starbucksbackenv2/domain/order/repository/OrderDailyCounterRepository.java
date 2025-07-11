package git_kkalnane.starbucksbackenv2.domain.order.repository;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderDailyCounter;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderDailyCounterId;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDailyCounterRepository extends JpaRepository<OrderDailyCounter, OrderDailyCounterId> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM OrderDailyCounter c WHERE c.id = :id")
    java.util.Optional<OrderDailyCounter> findByIdWithPessimisticLock(@org.springframework.data.repository.query.Param("id") OrderDailyCounterId id);
}