package git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class PointTransactionException extends BaseException {

    public PointTransactionException(
        PointTransactionErrorCode errorCode) {
        super(errorCode);
    }

    public PointTransactionException(PointTransactionErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
