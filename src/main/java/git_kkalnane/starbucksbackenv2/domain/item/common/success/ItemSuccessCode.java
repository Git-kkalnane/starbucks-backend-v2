package git_kkalnane.starbucksbackenv2.domain.item.common.success;

import git_kkalnane.starbucksbackenv2.global.success.SuccessCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ItemSuccessCode implements SuccessCode {

    DRINKS_LIST_RETRIEVED(HttpStatus.OK, "음료 목록 조회 성공"),
    DRINK_DETAIL_RETRIEVED(HttpStatus.OK, "음료 상세 조회 성공"),
    DESSERT_LIST_RETRIEVED(HttpStatus.OK, "디저트 목록 조회 성공"),
    DESSERT_DETAIL_RETRIEVED(HttpStatus.OK, "디저트 상세 조회 성공");

    private final HttpStatus status;
    private final String message;
}
