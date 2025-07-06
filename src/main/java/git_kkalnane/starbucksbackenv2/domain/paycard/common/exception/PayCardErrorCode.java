package git_kkalnane.starbucksbackenv2.domain.paycard.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PayCardErrorCode implements ErrorCode {
    PAY_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 카드가 존재하지 않습니다."),
    PAY_CARD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 보유 중인 결제 카드가 있습니다."),
    NOT_ENOUGH_PAY_CARD_AMOUNT(HttpStatus.BAD_REQUEST, "결제 카드의 잔액이 부족합니다."),
    FAIL_PAY_TRANSACTION(HttpStatus.BAD_REQUEST, "결제 실패했습니다."),
    PAY_CARD_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "결제 카드 생성에 실패했습니다. 다시 시도해주세요.");

    public static final String PREFIX = "[PAY_CARD ERROR] ";

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return PREFIX + rawMessage;
    }
}
