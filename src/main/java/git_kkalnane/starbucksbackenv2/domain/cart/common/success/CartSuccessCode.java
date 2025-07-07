package git_kkalnane.starbucksbackenv2.domain.cart.common.success;

import git_kkalnane.starbucksbackenv2.global.success.SuccessCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CartSuccessCode implements SuccessCode {

    CART_SUCCESS_CODE(HttpStatus.OK, "카트에 성공적으로 추가되었습니다."),
    CART_SUCCESS_MODIFIED(HttpStatus.OK, "수량이 성공적으로 수정되었습니다."),
    CART_SUCCESS_DELETED(HttpStatus.OK, "카트에서 성공적으로 삭제되었습니다");




    private final HttpStatus status;
    private final String message;

    CartSuccessCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
