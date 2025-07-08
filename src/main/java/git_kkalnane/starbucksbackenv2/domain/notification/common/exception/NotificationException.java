package git_kkalnane.starbucksbackenv2.domain.notification.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class NotificationException extends BaseException {

    public NotificationException(NotificationErrorCode errorCode) {
        super(errorCode);
    }

    public NotificationException(NotificationErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
