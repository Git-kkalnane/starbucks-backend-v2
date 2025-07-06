package git_kkalnane.starbucksbackenv2.domain.merchant.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class MerchantException extends BaseException {

    public MerchantException(MerchantErrorCode errorCode) {
        super(errorCode);
    }

    public MerchantException(MerchantErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }
}
