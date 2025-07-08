package git_kkalnane.starbucksbackenv2.domain.item.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.BaseException;

public class ItemException extends BaseException {
    public ItemException(ItemErrorCode errorCode) {
        super(errorCode);
    }
}
