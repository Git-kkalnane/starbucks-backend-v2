package git_kkalnane.starbucksbackenv2.domain.member.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class MemberException extends BaseException {
    public MemberException(MemberErrorCode errorCode) {
        super(errorCode);
    }
}
