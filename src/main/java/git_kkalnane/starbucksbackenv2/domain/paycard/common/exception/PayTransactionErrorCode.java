package git_kkalnane.starbucksbackenv2.domain.paycard.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PayTransactionErrorCode implements ErrorCode {
    POINT_TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "결제 이력이 존재하지 않습니다.");

    public static final String PREFIX = "[PAY_TRANSACTION ERROR] ";

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
