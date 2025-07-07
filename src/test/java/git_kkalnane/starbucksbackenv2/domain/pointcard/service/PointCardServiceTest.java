package git_kkalnane.starbucksbackenv2.domain.pointcard.service;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.PointTransactionType;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception.PointCardErrorCode;
import git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception.PointCardException;
import git_kkalnane.starbucksbackenv2.domain.pointcard.domain.PointCard;
import git_kkalnane.starbucksbackenv2.domain.pointcard.repository.PointCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PointCardServiceTest {

    @Mock
    private PointCardRepository pointCardRepository;
    @Mock
    private PointTransactionService pointTransactionService;

    @InjectMocks
    private PointCardService pointCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("calculateEarnedPoint: 결제 금액의 1%를 정상적으로 계산한다")
    void calculateEarnedPoint_success() {
        // given
        int paidAmount = 10000;
        int expected = 100;
        // when
        int result = pointCardService.calculateEarnedPoint(paidAmount);
        // then
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("calculateEarnedPoint: 음수 입력시 예외 발생")
    void calculateEarnedPoint_negative() {
        // given
        int negativeAmount = -100;
        // when & then
        assertThatThrownBy(() -> pointCardService.calculateEarnedPoint(negativeAmount))
                .isInstanceOf(PointCardException.class)
                .hasMessageContaining(PointCardErrorCode.PAYMENT_AMOUNT_IS_NEGATIVE.getRawMessage());
    }

    @Test
    @DisplayName("addPoint: 정상 입금 시 PointCard와 트랜잭션 생성")
    void addPoint_success() {
        // given
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        PointCard pointCard = PointCard.builder().member(member).pointAmount(0).build();
        when(pointCardRepository.findByMemberId(1L)).thenReturn(Optional.of(pointCard));
        when(pointCardRepository.save(any(PointCard.class))).thenReturn(pointCard);
        // when
        PointCard result = pointCardService.addPoint(1L, 500);
        // then
        assertThat(result.getPointAmount()).isEqualTo(500);
        verify(pointTransactionService, times(1)).createAndSaveTransaction(any(), eq(500), eq(PointTransactionType.DEPOSIT), anyString());
    }

    @Test
    @DisplayName("addPoint: 없는 카드에 입금 시 예외 발생")
    void addPoint_noCard() {
        // given
        when(pointCardRepository.findByMemberId(1L)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> pointCardService.addPoint(1L, 100))
                .isInstanceOf(PointCardException.class)
                .hasMessageContaining(PointCardErrorCode.POINT_CARD_NOT_FOUND.getRawMessage());
    }

    @Test
    @DisplayName("subtractPoint: 정상 출금 시 PointCard와 트랜잭션 생성, 잔액 감소")
    void subtractPoint_success() {
        // given
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        PointCard pointCard = PointCard.builder().member(member).pointAmount(1000).build();
        when(pointCardRepository.findByMemberId(1L)).thenReturn(Optional.of(pointCard));
        when(pointCardRepository.save(any(PointCard.class))).thenReturn(pointCard);
        // when
        PointCard result = pointCardService.subtractPoint(1L, 400);
        // then
        assertThat(result.getPointAmount()).isEqualTo(600);
        verify(pointTransactionService, times(1)).createAndSaveTransaction(any(), eq(400), eq(PointTransactionType.WITHDRAWAL), anyString());
    }

    @Test
    @DisplayName("subtractPoint: 잔액 부족 시 예외 발생")
    void subtractPoint_notEnoughBalance() {
        // given
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        PointCard pointCard = PointCard.builder().member(member).pointAmount(200).build();
        when(pointCardRepository.findByMemberId(1L)).thenReturn(Optional.of(pointCard));
        // when & then
        assertThatThrownBy(() -> pointCardService.subtractPoint(1L, 500))
                .isInstanceOf(PointCardException.class)
                .hasMessageContaining(PointCardErrorCode.NOT_ENOUGH_POINT_CARD_AMOUNT.getRawMessage());
    }

    @Test
    @DisplayName("subtractPoint: 없는 카드에 출금 시 예외 발생")
    void subtractPoint_noCard() {
        // given
        when(pointCardRepository.findByMemberId(1L)).thenReturn(Optional.empty());
        // when & then
        assertThatThrownBy(() -> pointCardService.subtractPoint(1L, 100))
                .isInstanceOf(PointCardException.class)
                .hasMessageContaining(PointCardErrorCode.POINT_CARD_NOT_FOUND.getRawMessage());
    }
}

