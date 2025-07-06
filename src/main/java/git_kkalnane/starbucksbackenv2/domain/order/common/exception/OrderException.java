package git_kkalnane.starbucksbackenv2.domain.order.common.exception;


import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class OrderException extends BaseException {
    public OrderException(OrderErrorCode errorCode) { super(errorCode);}
}
