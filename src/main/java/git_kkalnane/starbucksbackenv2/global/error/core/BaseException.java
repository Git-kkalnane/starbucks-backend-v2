package git_kkalnane.starbucksbackenv2.global.error.core;

import lombok.Getter;

@Getter
public abstract class BaseException extends RuntimeException {

    private final ErrorCode errorCode;

    public BaseException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BaseException(ErrorCode errorCode, Object ... args) {
        super(errorCode.getMessage(args));
        this.errorCode = errorCode;
    }
}
