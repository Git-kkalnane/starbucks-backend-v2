package git_kkalnane.starbucksbackenv2.domain.member.event;


import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.domain.cart.service.CartService;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.paycard.common.exception.PayCardErrorCode;
import git_kkalnane.starbucksbackenv2.domain.paycard.common.exception.PayCardException;
import git_kkalnane.starbucksbackenv2.domain.paycard.service.PayCardService;
import git_kkalnane.starbucksbackenv2.domain.pointcard.service.PointCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class MemberSignupEventListener {

    private final PayCardService payCardService;
    private final PointCardService pointCardService;
    private final CartService cartService;

    /**
     * 회원가입 완료 후 PayCard를 생성하는 이벤트 리스너
     * 트랜잭션이 성공적으로 커밋된 후에 실행됩니다.
     *
     * @param event 회원가입 완료 이벤트
     */
    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, classes = MemberSignedUpEvent.class)
    public void handleMemberSignedUpEvent(MemberSignedUpEvent event) {
        Member member = event.getMember();
        createPayCardForMember(member);
        createPointCardForMember(member);
        createCartForMember(member);
    }

    /**
     * 회원의 PayCard를 생성하고 예외를 처리합니다.
     */
    private void createPayCardForMember(Member member) {
        try {
            payCardService.createPayCard(member);
            log.info("PayCard 생성 - 회원: {}", member.getEmail());
        } catch (PayCardException e) {
            if (e.getErrorCode() == PayCardErrorCode.PAY_CARD_ALREADY_EXISTS) {
                log.warn("PayCard 이미 존재 - 회원 : {}", member.getEmail());
            } else {
                log.error("PayCard 생성 실패 - 회원: {}. 오류: {}", member.getEmail(), e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("PayCard 생성 중 예상치 못한 오류가 발생했습니다. 회원: {}", member.getEmail(), e);
        }
    }


    /**
     * 회원의 PointCard를 생성하고 예외를 처리합니다.
     */
    private void createPointCardForMember(Member member) {
        try {
            pointCardService.createPointCard(member);
            log.info("PointCard 생성 - 회원: {}", member.getEmail());
        } catch (PayCardException e) {
            if (e.getErrorCode() == PayCardErrorCode.PAY_CARD_ALREADY_EXISTS) {
                log.warn("PointCard 이미 존재 - 회원 : {}", member.getEmail());
            } else {
                log.error("PointCard 생성 실패 - 회원: {}. 오류: {}", member.getEmail(), e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("PointCard 생성 중 예상치 못한 오류가 발생했습니다. 회원: {}", member.getEmail(), e);
        }

    }

    private void createCartForMember(Member member) {
        try {
            cartService.createCartForMember(member);
            log.info("Cart 생성 - 회원: {}", member.getEmail());
        } catch (PayCardException e) {
            if(e.getErrorCode() == CartErrorCode.CART_ALREADY_EXISTS) {
                log.warn("Cart 이미 존재 - 회원 : {}", member.getEmail());
            } else {
                log.error("Cart 생성 실패 - 회원 {}. 오류: {}", member.getEmail(), e.getMessage(), e);
            }
        } catch (Exception e) {
            log.error("Cart 생성 중 예상치 못한 오류가 발생했습니다. 회원: {}", member.getEmail(), e);
        }
    }
}

