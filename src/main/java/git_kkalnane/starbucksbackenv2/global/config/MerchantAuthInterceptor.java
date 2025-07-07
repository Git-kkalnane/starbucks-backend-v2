package git_kkalnane.starbucksbackenv2.global.config;

import git_kkalnane.starbucksbackenv2.domain.auth.common.exception.AuthErrorCode;
import git_kkalnane.starbucksbackenv2.domain.auth.common.exception.AuthException;
import git_kkalnane.starbucksbackenv2.domain.auth.common.jwt.utils.TokenParser;
import git_kkalnane.starbucksbackenv2.domain.auth.merchant.service.MerchantAuthService;
import git_kkalnane.starbucksbackenv2.global.utils.GlobalLogger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class MerchantAuthInterceptor implements HandlerInterceptor {

    private static final String REQUEST_URI_LOG_PREFIX = "[Intercept] 요청 경로 정보: ";
    private static final String MERCHANT_ID_LOG_PREFIX = "[Intercept] merchantId: ";

    private final MerchantAuthService merchantAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        GlobalLogger.info(REQUEST_URI_LOG_PREFIX, request.getMethod(), request.getRequestURI());

        // HTTP 요청의 Authorization 헤더의 값을 가져온다.
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateBearerToken(bearerToken);

        // Authorization 헤더의 값에서 접두사(Bearer)를 제거한다.
        String plainToken = TokenParser.removeBearerTokenPrefix(bearerToken);

        Long merchantId = merchantAuthService.verifyTokenIncludedInRequest(plainToken);

        // HTTP 요청의 속성에 멤버 엔티티의 식별자를 추가한다.
        request.setAttribute("storeId", merchantId);

        GlobalLogger.info(MERCHANT_ID_LOG_PREFIX, merchantId);

        return true;
    }

    /**
     * Bearer 토큰의 값을 검증하는 메서드
     * <br><br>
     * Authorization 헤더에 있는 accessToken이 null이거나 빈 문자열인지 검증한다.
     *
     * @param bearerToken Authorization 헤더에 있는 bearerToken
     */
    private void validateBearerToken(String bearerToken) {

        // 토큰이 존재하지 않거나 빈 문자열일 경우 401 에러코드 반환
        if (bearerToken == null || bearerToken.isBlank()) {
            throw new AuthException(AuthErrorCode.UNAUTHORIZED_ACCESS);
        }
    }
}
