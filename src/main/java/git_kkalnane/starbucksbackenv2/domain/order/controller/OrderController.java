package git_kkalnane.starbucksbackenv2.domain.order.controller;

import git_kkalnane.starbucksbackenv2.domain.order.common.success.OrderSuccessCode;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderCreateRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.StoreOrderStatusUpdateRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.response.*;
import git_kkalnane.starbucksbackenv2.domain.order.service.OrderService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 주문 관련 API 컨트롤러
 * 주문 생성, 수정, 삭제 담당
 *
 * version 2.0
 */

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "Order", description = "주문 관련 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문 생성", description = "로그인한 사용자가 상품을 주문합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "주문 생성 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터 유효성 오류"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "404", description = "사용자, 매장 또는 상품을 찾을 수 없음")
    })
    @PostMapping("/orders")
    public ResponseEntity<SuccessResponse<OrderCreateResponse>> createOrder(
            @RequestAttribute(name = "memberId") Long memberId,
            @Parameter(description = "주문에 필요한 정보") @Valid @RequestBody OrderCreateRequest request) {

        Order savedOrder = orderService.createOrder(request, memberId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(SuccessResponse.of(
                        OrderSuccessCode.ORDER_SUCCESS_CREATED,
                        new OrderCreateResponse(savedOrder.getId())
                ));
    }

    @Operation(summary = "[사용자용] 주문 상세 조회", description = "사용자가 특정 주문의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상세 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자"),
            @ApiResponse(responseCode = "403", description = "해당 주문에 접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "주문 찾을 수 없음")
    })
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderDetailResponse>> getOrderDetail(
            @RequestAttribute(name = "memberId") Long loginMemberId,
            @Parameter(description = "조회할 주문의 ID") @PathVariable Long orderId) {

        OrderDetailResponse responseDto = orderService.getOrderDetail(loginMemberId, orderId);

        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.ORDER_DETAIL_VIEWED, responseDto));
    }

    @Operation(summary = "[사용자용] 현재 주문 목록 조회", description = "현재 로그인한 사용자의 진행중인(접수, 준비중, 픽업 가능) 모든 주문 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/orders/me/current")
    public ResponseEntity<SuccessResponse<List<CustomerCurrentOrderResponse>>> getCurrentOrders(
            @RequestAttribute(name = "memberId") Long memberId) {

        List<CustomerCurrentOrderResponse> result = orderService.getCurrentOrders(memberId);
        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.ORDER_CURRENT_VIEWED, result));
    }

    @Operation(summary = "[사용자용] 나의 과거 주문 내역 조회",
            description = "현재 로그인한 사용자의 과거 주문(완료/취소) 목록을 페이지네이션하여 조회합니다. **(인증 필수)**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "과거 주문 내역 조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자")
    })
    @GetMapping("/orders/me/history")
    public ResponseEntity<SuccessResponse<CustomerOrderHistoryListResponse>> getMyOrderHistory(
            @RequestAttribute(name = "memberId") Long memberId,
            @Parameter(hidden = true)
            @PageableDefault(size = 15, sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        CustomerOrderHistoryListResponse responseDto = orderService.getOrderHistory(memberId, pageable);

        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.ORDER_HISTORY_VIEWED, responseDto));
    }

    @Operation(summary = "[매장용] 현재 주문 목록 조회",
            description = "로그인한 매장의 진행중인(접수, 준비중, 픽업 가능) 주문 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (매장)"),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음")
    })
    @GetMapping("/stores/{storeId}/orders/current")
    public ResponseEntity<SuccessResponse<List<StoreCurrentOrderResponse>>> getStoreCurrentOrders(
            @RequestAttribute(name = "storeId") Long storeId) {

        List<StoreCurrentOrderResponse> responseDto = orderService.getStoreCurrentOrders(storeId);

        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.STORE_ORDERS_VIEWED, responseDto));
    }

    @Operation(summary = "[매장용] 특정 주문 상세 조회",
            description = "로그인한 매장의 특정 주문 하나에 대한 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (매장)"),
            @ApiResponse(responseCode = "403", description = "해당 주문에 대한 접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @GetMapping("/stores/{storeId}/orders/{orderId}")
    public ResponseEntity<SuccessResponse<OrderDetailResponse>> getStoreOrderDetail(
            @Parameter(description = "주문이 속한 매장의 ID") @PathVariable Long storeId,
            @Parameter(description = "조회할 주문의 ID") @PathVariable Long orderId
    ) {
        OrderDetailResponse responseDto = orderService.getStoreOrderDetail(storeId, orderId);

        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.STORE_ORDER_DETAIL_VIEWED, responseDto));
    }

    @Operation(summary = "[매장용] 과거 주문 내역 조회",
            description = "로그인한 매장의 과거 주문(완료/취소) 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (매장)"),
            @ApiResponse(responseCode = "404", description = "매장을 찾을 수 없음")
    })
    @GetMapping("/stores/{storeId}/orders/history")
    public ResponseEntity<SuccessResponse<StoreOrderHistoryListResponse>> getStoreOrderHistory(
            @RequestAttribute(name = "storeId") Long storeId,
            @Parameter(hidden = true)
            @PageableDefault(size = 15, sort = "modifiedAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        StoreOrderHistoryListResponse responseDto = orderService.getStoreOrderHistory(storeId, pageable);

        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.STORE_ORDER_HISTORY_VIEWED, responseDto));
    }

    @Operation(summary = "[매장용] 주문 상태 변경",
            description = "로그인한 매장의 특정 주문 상태를 변경합니다. (예: 접수 -> 준비중) **(인증 필수)**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "주문 상태 변경 성공"),
            @ApiResponse(responseCode = "400", description = "요청 데이터가 유효하지 않거나, 변경할 수 없는 주문 상태"),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (매장)"),
            @ApiResponse(responseCode = "403", description = "해당 주문에 대한 접근 권한 없음"),
            @ApiResponse(responseCode = "404", description = "주문을 찾을 수 없음")
    })
    @PatchMapping("/stores/{storeId}/orders/{orderId}/status")
    public ResponseEntity<SuccessResponse<?>> updateOrderStatus(
            @Parameter(description = "주문이 속한 매장의 ID") @PathVariable Long storeId,
            @Parameter(description = "상태를 변경할 주문의 ID") @PathVariable Long orderId,
            @Parameter(description = "새로운 주문 상태 정보") @Valid @RequestBody StoreOrderStatusUpdateRequest request
    ) {
        orderService.updateOrderStatus(storeId, orderId, request.newStatus());

        return ResponseEntity
                .ok(SuccessResponse.of(OrderSuccessCode.ORDER_STATUS_UPDATED));
    }

}