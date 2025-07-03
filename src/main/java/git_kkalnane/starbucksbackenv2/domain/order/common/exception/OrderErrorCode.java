package git_kkalnane.starbucksbackenv2.domain.order.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum OrderErrorCode implements ErrorCode {

    ORDER_ERROR_CODE(HttpStatus.BAD_REQUEST, "주문생성이 실패했습니다."),
    INVALID_ORDER_ITEM_REFERENCE(HttpStatus.BAD_REQUEST, "주문 상품이 유효한 메뉴를 참조하지 않습니다."),
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 주문을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 매장을 찾을 수 없습니다."),
    FORBIDDEN_ACCESS_ORDER(HttpStatus.FORBIDDEN, "해당 주문에 접근할 권한이 없습니다."),
    CANNOT_UPDATE_COMPLETED_ORDER(HttpStatus.BAD_REQUEST, "이미 완료되거나 취소된 주문의 상태는 변경할 수 없습니다."),
    ORDER_TOTAL_PRICE_MISMATCH(HttpStatus.BAD_REQUEST, "주문 총액이 상품 가격의 합계와 일치하지 않습니다."),
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 상품을 찾을 수 없습니다."),
    ITEM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "요청하신 상품 옵션을 찾을 수 없습니다."),
    INVALID_ITEM_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 상품 타입입니다.");

    private final HttpStatus status;
    private final String message;

    OrderErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
