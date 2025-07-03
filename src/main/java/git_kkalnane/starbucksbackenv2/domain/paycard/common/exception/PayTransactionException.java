package git_kkalnane.starbucksbackenv2.domain.paycard.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class PayTransactionException extends BaseException {

    public PayTransactionException(PayTransactionErrorCode errorCode) {
        super(errorCode);
    }

    public PayTransactionException(PayTransactionErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
