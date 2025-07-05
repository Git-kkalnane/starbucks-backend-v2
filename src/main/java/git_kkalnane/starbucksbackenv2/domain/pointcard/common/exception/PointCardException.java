package git_kkalnane.starbucksbackenv2.domain.pointcard.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class PointCardException extends BaseException {

    public PointCardException(PointCardErrorCode errorCode) {
        super(errorCode);
    }

    public PointCardException(PointCardErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
