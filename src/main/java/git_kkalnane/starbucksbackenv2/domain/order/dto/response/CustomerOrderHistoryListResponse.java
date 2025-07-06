package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 사용자의 과거 주문 내역 목록과 페이지네이션 정보를 함께 담는 최종 응답 DTO.
 *
 * @param orders        주문 요약 정보 리스트
 * @param currentPage   현재 페이지 번호
 * @param totalPages    전체 페이지 수
 * @param totalElements 전체 주문 개수
 */
public record CustomerOrderHistoryListResponse(
        List<CustomerOrderHistoryResponse> orders,
        int currentPage,
        int totalPages,
        long totalElements
) {
    public static CustomerOrderHistoryListResponse from(Page<Order> orderPage) {
        List<CustomerOrderHistoryResponse> orderResponses = orderPage.getContent().stream()
                .map(CustomerOrderHistoryResponse::from)
                .toList();

        return new CustomerOrderHistoryListResponse(
                orderResponses,
                orderPage.getNumber(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );
    }
}