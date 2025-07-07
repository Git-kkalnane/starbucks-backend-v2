package git_kkalnane.starbucksbackenv2.domain.pointcard.service;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.PointTransactionType;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception.PointCardErrorCode;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception.PointCardException;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointTransaction;
import git_kkalnane.starbucksbackenv2.domain.pointcard.repository.PointCardRepository;
import git_kkalnane.starbucksbackenv2.domain.pointcard.repository.PointTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointCardService {

    private final PointCardRepository pointCardRepository;
    private final PointTransactionService pointTransactionService;

    private int initPointCardAmount = 0;

    /**
     * 새로운 PayCard를 생성하고 초기 잔액을 설정합니다. 한 회원당 하나의 PayCard만 가질 수 있습니다.
     *
     * @param member PayCard를 생성할 회원
     * @return 생성된 PayCard 객체
     * @throws PointCardException 이미 PayCard가 존재하는 경우 발생
     */
    @Transactional
    public PointCard createPointCard(Member member) {
        // 이미 PointCard가 있는지 확인
        if (pointCardRepository.existsByMemberId(member.getId())) {
            throw new PointCardException(PointCardErrorCode.POINT_CARD_ALREADY_EXISTS);
        }

        // 초기 잔액으로 PointCard 생성 및 저장
        PointCard pointCard = PointCard.builder()
            .cardNumber(member.getEmail()) // TODO card email 고민하기
            .pointAmount(initPointCardAmount)
            .member(member)
            .build();

        return pointCardRepository.save(pointCard);
    }

    /**
     * 포인트 입금 메서드
     *
     * @param memberId 입금 대상 카드
     * @param amount   입금할 포인트
     * @return 입금 후 PointCard
     */
    @Transactional
    public PointCard addPoint(Long memberId, int amount) {
        PointCard pointCard = pointCardRepository.findByMemberId(memberId)
            .orElseThrow(() -> new PointCardException(PointCardErrorCode.POINT_CARD_NOT_FOUND));

        pointCard.addPointAmount(amount);
        pointTransactionService.createAndSaveTransaction(
            pointCard,
            amount,
            PointTransactionType.DEPOSIT,
            getDescription(amount, PointTransactionType.DEPOSIT)
        );
        return pointCardRepository.save(pointCard);
    }

    /**
     * 포인트 출금 메서드
     *
     * @param memberId 출금 대상 카드
     * @param amount   출금할 포인트
     * @return 출금 후 PointCard
     */
    @Transactional
    public PointCard subtractPoint(Long memberId, int amount) {
        PointCard pointCard = pointCardRepository.findByMemberId(memberId)
            .orElseThrow(() -> new PointCardException(PointCardErrorCode.POINT_CARD_NOT_FOUND));
        if (pointCard.getPointAmount() < amount) {
            throw new PointCardException(PointCardErrorCode.NOT_ENOUGH_POINT_CARD_AMOUNT);
        }
        
        pointCard.subtractPointAmount(amount);
        pointTransactionService.createAndSaveTransaction(
            pointCard,
            amount,
            PointTransactionType.WITHDRAWAL,
            getDescription(amount, PointTransactionType.WITHDRAWAL)
        );
        return pointCardRepository.save(pointCard);
    }


    private String getDescription(int amount, PointTransactionType type) {
        return "" + amount + type.getKorean();
    }   

    /**
     * 결제 금액의 1%를 포인트로 적립하는 금액을 계산합니다.
     *
     * @param paidAmount 결제한 금액 (음수일 수 없음)
     * @return 적립될 포인트 금액 (정수)
     * @throws IllegalArgumentException 결제 금액이 음수일 경우 발생
     */
    public int calculateEarnedPoint(int paidAmount) {
    if (paidAmount < 0) {
        throw new PointCardException(PointCardErrorCode.PAYMENT_AMOUNT_IS_NEGATIVE);
    }
    return paidAmount / 100;
}

}
