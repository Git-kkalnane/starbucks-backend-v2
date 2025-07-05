package git_kkalnane.starbucksbackenv2.domain.pointcard.common.success;

import git_kkalnane.starbucksbackenv2.global.success.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PointCardSuccessCode implements SuccessCode {
    POINT_CARD_CREATE(HttpStatus.CREATED, "포인트 카드가 성공적으로 생성되었습니다."),
    GET_POINT_CARD_DETAIL_INFO_COMPLETE(HttpStatus.OK,"포인트 조회에 성공했습니다."),
    POINT_CARD_UPDATE_COMPLETE(HttpStatus.OK,"포인트가 성공적으로 입금되었습니다."),
    POINT_CARD_WITHDRAWAL_COMPLETE(HttpStatus.OK,"포인트가 성공적으로 출금되었습니다.");

    private final HttpStatus status;
    private final String rawMessage;

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return  rawMessage;
    }
}
