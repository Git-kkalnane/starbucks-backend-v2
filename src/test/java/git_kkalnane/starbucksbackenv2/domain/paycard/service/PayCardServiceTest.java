package git_kkalnane.starbucksbackenv2.domain.paycard.service;

import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.paycard.common.exception.PayCardErrorCode;
import git_kkalnane.starbucksbackenv2.domain.paycard.common.exception.PayCardException;
import git_kkalnane.starbucksbackenv2.domain.paycard.domain.PayCard;
import git_kkalnane.starbucksbackenv2.domain.paycard.repository.PayCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class PayCardServiceTest {

    @Mock
    private PayCardRepository payCardRepository;

    @InjectMocks
    private PayCardService payCardService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // initPayCardAmount 필드 값 강제 주입 (Reflection)
        ReflectionTestUtils.setField(payCardService, "initPayCardAmount", 500000);
    }

    @Test
    @DisplayName("회원이 PayCard를 정상적으로 생성할 수 있다")
    void createPayCard_success() {
        // given
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(payCardRepository.existsByMemberId(1L)).thenReturn(false);

        ArgumentCaptor<PayCard> payCardCaptor = ArgumentCaptor.forClass(PayCard.class);
        when(payCardRepository.save(payCardCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        PayCard payCard = payCardService.createPayCard(member);

        // then
        verify(payCardRepository).save(any(PayCard.class));
        assertThat(payCard.getCardAmount()).isEqualTo(500000);
        assertThat(payCard.getMember()).isEqualTo(member);
        assertThat(payCard.getCardNumber()).isNotNull();
        assertThat(payCard.getCardNumber().replace(" ", "")).hasSize(16);
    }

    @Test
    @DisplayName("이미 PayCard가 존재하면 예외가 발생한다")
    void createPayCard_alreadyExists() {
        // given
        Member member = mock(Member.class);
        when(member.getId()).thenReturn(1L);
        when(payCardRepository.existsByMemberId(1L)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> payCardService.createPayCard(member))
                .isInstanceOf(PayCardException.class)
                .hasMessageContaining(PayCardErrorCode.PAY_CARD_ALREADY_EXISTS.getMessage());
    }
}
