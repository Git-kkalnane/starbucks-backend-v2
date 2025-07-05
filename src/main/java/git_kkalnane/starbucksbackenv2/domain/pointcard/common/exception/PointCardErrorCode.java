package git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PointCardErrorCode implements ErrorCode {
    POINT_CARD_NOT_FOUND(HttpStatus.NOT_FOUND, "포인트 카드가 존재하지 않습니다."),
    POINT_CARD_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 보유 중인 포인트 카드가 있습니다."),
    NOT_ENOUGH_POINT_CARD_AMOUNT(HttpStatus.BAD_REQUEST, "포인트 잔액이 부족합니다."),
    FAIL_POINT_TRANSACTION(HttpStatus.BAD_REQUEST, "포인트 사용 실패했습니다."),
    POINT_CARD_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "포인트 카드 생성에 실패했습니다. 다시 시도해주세요.");

    public static final String PREFIX = "[POINT_CARD ERROR] ";

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
