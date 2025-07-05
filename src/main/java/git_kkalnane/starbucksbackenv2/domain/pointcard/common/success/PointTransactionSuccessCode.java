package git_kkalnane.starbucksbackenv2.domain.pointcard.common.success;

import git_kkalnane.starbucksbackenv2.global.success.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PointTransactionSuccessCode implements SuccessCode {
    POINT_TRANSACTION_CREATE(HttpStatus.CREATED, "포인트 이력이 성공적으로 생성되었습니다."),
    GET_POINT_TRANSACTION_DETAIL_INFO_COMPLETE(HttpStatus.OK,"포인트 이력 조회에 성공했습니다."),
    POINT_TRANSACTION_UPDATE_COMPLETE(HttpStatus.OK,"포인트 이력이 성공적으로 업데이트되었습니다."),
    POINT_TRANSACTION_DELETE_COMPLETE(HttpStatus.OK,"포인트 이력이 성공적으로 삭제되었습니다.");

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
