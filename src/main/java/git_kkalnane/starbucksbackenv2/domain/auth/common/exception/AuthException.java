package git_kkalnane.starbucksbackenv2.domain.auth.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class AuthException extends BaseException {

    public AuthException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthException(AuthErrorCode errorCode, Object ... args) {
        super(errorCode,args);
    }
}
