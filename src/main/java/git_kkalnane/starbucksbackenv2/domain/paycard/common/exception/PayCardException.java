package git_kkalnane.starbucksbackenv2.domain.paycard.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class PayCardException extends BaseException {

    public PayCardException(PayCardErrorCode errorCode) {
        super(errorCode);
    }

    public PayCardException(PayCardErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
