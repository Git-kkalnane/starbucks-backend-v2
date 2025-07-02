package git_kkalnane.starbucksbackenv2.domain.auth.common.success;

import git_kkalnane.starbucksbackenv2.global.success.SuccessCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AuthSuccessCode implements SuccessCode {

    LOGIN_COMPLETED(HttpStatus.OK, "로그인에 성공하였습니다."),
    LOGOUT_COMPLETED(HttpStatus.OK, "로그아웃에 성공하였습니다."),
    UNLINK_COMPLETED(HttpStatus.OK, "회원 탈퇴에 성공하였습니다."),
    TOKEN_REISSUE_COMPLETED(HttpStatus.CREATED, "토큰 재발급에 성공하였습니다."),
    AUTHORIZED(HttpStatus.OK, "인증에 성공하였습니다.");

    private final HttpStatus status;
    private final String message;
}
