package git_kkalnane.starbucksbackenv2.domain.pointcard.service;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.paycard.common.exception.PayCardErrorCode;
import git_kkalnane.starbucksbackenv2.domain.paycard.common.exception.PayCardException;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointTransaction;
import git_kkalnane.starbucksbackenv2.domain.pointcard.repository.PointCardRepository;
import git_kkalnane.starbucksbackenv2.domain.pointcard.repository.PointTransactionRepository;
import git_kkalnane.starbucksbackenv2.domain.paycard.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointCardService {
    private final PointCardRepository pointCardRepository;
    private final PointTransactionRepository pointTransactionRepository;

    private int initPointCardAmount=0;

    /**
     * 새로운 PayCard를 생성하고 초기 잔액을 설정합니다.
     * 한 회원당 하나의 PayCard만 가질 수 있습니다.
     *
     * @param member PayCard를 생성할 회원
     * @return 생성된 PayCard 객체
     * @throws PayCardException 이미 PayCard가 존재하는 경우 발생
     */
    @Transactional
    public PointCard createPointCard(Member member) {
        // 이미 PayCard가 있는지 확인
        if (pointCardRepository.existsByMemberId(member.getId())) {
            throw new PayCardException(PayCardErrorCode.PAY_CARD_ALREADY_EXISTS);
        }

        // 초기 잔액으로 PayCard 생성 및 저장
       PointCard pointCard = PointCard.builder()
           .cardNumber(member.getEmail()) // TODO card email 고민하기
           .pointAmount(initPointCardAmount)
           .member(member)
        .build();

        return pointCardRepository.save(pointCard);
    }
    
    /**
     * 포인트 입금 메서드
     * @param pointCard 입금 대상 카드
     * @param amount 입금할 포인트
     * @return 입금 후 PointCard
     */
    @Transactional
    public PointCard addPoint(PointCard pointCard, int amount) {
        pointCard.addPointAmount(amount);
        PointTransaction transaction = PointTransaction.builder()
            .pointCard(pointCard)
            .payAmount((long) amount)
            .type(TransactionType.DEPOSIT)
            .description("포인트 입금")
            .build();
        pointTransactionRepository.save(transaction);
        return pointCardRepository.save(pointCard);
    }

    /**
     * 포인트 출금 메서드
     * @param pointCard 출금 대상 카드
     * @param amount 출금할 포인트
     * @return 출금 후 PointCard
     */
    @Transactional
    public PointCard subtractPoint(PointCard pointCard, int amount) {
        pointCard.subtractPointAmount(amount);
        PointTransaction transaction = PointTransaction.builder()
            .pointCard(pointCard)
            .payAmount((long) amount)
            .type(TransactionType.WITHDRAWAL)
            .description("포인트 출금")
            .build();
        pointTransactionRepository.save(transaction);
        return pointCardRepository.save(pointCard);
    }

}
