package git_kkalnane.starbucksbackenv2.domain.merchant.controller;

import git_kkalnane.starbucksbackenv2.domain.member.dto.response.SignUpResponse;
import git_kkalnane.starbucksbackenv2.domain.merchant.common.success.MerchantSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.merchant.dto.request.SignUpRequest;
import git_kkalnane.starbucksbackenv2.domain.merchant.service.MerchantService;
import git_kkalnane.starbucksbackenv2.domain.notification.common.success.NotificationSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.notification.domain.NotificationTargetType;
import git_kkalnane.starbucksbackenv2.domain.notification.dto.request.OrderNotificationSendRequest;
import git_kkalnane.starbucksbackenv2.domain.notification.service.NotificationService;
import git_kkalnane.starbucksbackenv2.global.success.SuccessResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/merchant")
@RequiredArgsConstructor
@Tag(name = "Merchant", description = "점주 관련 API")
public class MerchantController {

    private final MerchantService merchantService;
    private final NotificationService notificationService;

    /**
     * HTTP Request Body에 전송된 정보를 이용해 회원가입 요청을 처리하는 컨트롤러 메서드이다.
     *
     * @param request SingUpRequest 객체
     * @return SignUpResponse 객체를 담고 있는 ResponseEntity 객체
     */
    @Operation(
            summary = "점주 회원가입",
            description = "점주의 정보를 받아 회원가입을 처리합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "점주 회원가입 성공"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 데이터 유효성 오류 (형식, 길이, 규칙 위반 등)"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "이미 존재하는 이메일 또는 사업자 등록번호"
            )
    })
    @PostMapping("/signup")
    public ResponseEntity<SuccessResponse<SignUpResponse>> signup(
            @Parameter(description = "점주 회원가입에 필요한 정보") @RequestBody @Valid SignUpRequest request) {

        merchantService.createMerchant(request);

        return ResponseEntity.ok(
                (SuccessResponse.of(MerchantSuccessCode.SIGN_UP_COMPLETED)));
    }

    @PostMapping
    @Operation(summary = "멤버에 알림 전송 요청"
            , description = "특정 멤버에게 알림 전송을 요청합니다. 매장에서 멤버에 알림 전송을 요청할 때 쓰입니다.")
    @ApiResponses(
            value = {
                   @ApiResponse(
                           responseCode = "200",
                           description = "알림 전송 성공"
                   ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "알림 전송 실패(Broken pipe)"
                    )
            }

    )
    public ResponseEntity<SuccessResponse<?>> notificationRequest(@RequestBody OrderNotificationSendRequest request) {
        request.setNotificationTargetType(NotificationTargetType.MEMBER);

        notificationService.sendNotification(request);
        return ResponseEntity.ok(SuccessResponse.of(NotificationSuccessCode.NOTIFICATION_DELIVERED));
    }

    @GetMapping
    @Operation(summary = "지점 알림 목록 조회"
            , description = "지점의 알림 목록을 조회합니다. 조회되지 않으면 빈 리스트를 반환합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "알림 목록 조회 완료"
    )
    public ResponseEntity<SuccessResponse<?>> fetchNotifications(
            @RequestAttribute Long receiverId,
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {

        return ResponseEntity.ok(SuccessResponse.of(
                NotificationSuccessCode.NOTIFICATION_SUBSCRIPTION_RETRIEVED
                , notificationService.fetchNotifications(receiverId, NotificationTargetType.MERCHANT, pageable)));
    }
}
