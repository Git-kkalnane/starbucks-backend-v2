package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import org.springframework.data.domain.Page;

import java.util.List;

public record StoreOrderHistoryListResponse(
        List<StoreHistoryOrderResponse> orders,
        int currentPage,
        int totalPages,
        long totalElements
) {
    public static StoreOrderHistoryListResponse from(Page<Order> orderPage) {
        List<StoreHistoryOrderResponse> orderResponses = orderPage.getContent().stream()
                .map(StoreHistoryOrderResponse::from)
                .toList();

        return new StoreOrderHistoryListResponse(
                orderResponses,
                orderPage.getNumber(),
                orderPage.getTotalPages(),
                orderPage.getTotalElements()
        );
    }
}