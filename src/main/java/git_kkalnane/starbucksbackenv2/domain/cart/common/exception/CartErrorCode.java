package git_kkalnane.starbucksbackenv2.domain.cart.common.exception;

import git_kkalnane.starbucksbackenv2.domain.cart.common.exception.CartErrorCode;
import git_kkalnane.starbucksbackenv2.global.error.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum CartErrorCode implements ErrorCode {

    CART_ERROR_CODE(HttpStatus.BAD_REQUEST, "카트에러"),
    CART_NOT_FOUND(HttpStatus.NOT_FOUND, "카트가 존재하지 않습니다."),
    CART_ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "카트 아이템이 존재하지 않습니다."),
    CART_INVALID(HttpStatus.BAD_REQUEST, "본인의 장바구니만 수정할 수 있습니다."),
    CART_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "카트가 이미 생성되었습니다."),
    INVALID_TYPE(HttpStatus.BAD_REQUEST, "아이템 타입이 존재하지 않는 타입입니다."),
    INVALID_ITEM(HttpStatus.BAD_REQUEST, "존재하지 않는 아이템입니다.");

    private final HttpStatus status;
    private final String message;

    CartErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}