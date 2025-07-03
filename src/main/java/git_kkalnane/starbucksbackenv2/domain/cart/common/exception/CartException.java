package git_kkalnane.starbucksbackenv2.domain.cart.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class CartException extends BaseException {
    public CartException(CartErrorCode errorCode) {super(errorCode);}
}