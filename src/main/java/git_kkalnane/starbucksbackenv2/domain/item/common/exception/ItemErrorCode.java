package git_kkalnane.starbucksbackenv2.domain.item.common.exception;

import git_kkalnane.starbucksbackenv2.global.error.core.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ItemErrorCode implements ErrorCode {

    BEVERAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 음료입니다."),
    DESSERT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 디저트입니다."),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 메뉴입니다."),
    OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 옵션입니다.");

    private final HttpStatus status;
    private final String message;
}
