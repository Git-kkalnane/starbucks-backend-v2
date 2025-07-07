package git_kkalnane.starbucksbackenv2.domain.pointcard.service;

import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointTransaction;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.PointTransactionType;
import git_kkalnane.starbucksbackenv2.domain.pointcard.repository.PointTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PointTransactionService {
    private final PointTransactionRepository pointTransactionRepository;

    public PointTransactionService(PointTransactionRepository pointTransactionRepository) {
        this.pointTransactionRepository = pointTransactionRepository;
    }

    /**
     * 포인트 트랜잭션을 생성 및 저장합니다.
     *
     * @param pointCard 트랜잭션 대상 포인트카드
     * @param amount    트랜잭션 금액
     * @param type      트랜잭션 타입 (입금/출금)
     * @param description 트랜잭션 설명
     * @return 생성된 PointTransaction
     */
    @Transactional
    public PointTransaction createAndSaveTransaction(PointCard pointCard, int amount, PointTransactionType type, String description) {
        PointTransaction transaction = PointTransaction.builder()
                .pointCard(pointCard)
                .payAmount((long) amount)
                .type(type)
                .description(description)
                .build();
        return pointTransactionRepository.save(transaction);
    }
}
