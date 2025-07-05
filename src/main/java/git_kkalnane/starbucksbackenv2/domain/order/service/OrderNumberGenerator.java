package git_kkalnane.starbucksbackenv2.domain.order.service;

import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderDailyCounter;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderDailyCounterId;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderCreateRequest;
import git_kkalnane.starbucksbackenv2.domain.order.repository.OrderDailyCounterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class OrderNumberGenerator {
    private final OrderDailyCounterRepository orderDailyCounterRepository;

    /**
     * 해당 매장과 날짜 기준으로 고유한 주문번호를 생성합니다.
     * 일별 카운터를 증가시키고 주문번호 문자열을 반환합니다.
     *
     * @param request 주문 생성 요청(매장 ID 포함)
     * @return 생성된 주문번호 (예: "A-1")
     */
    @Transactional
    public String generateOrderNumber(OrderCreateRequest request) {
        LocalDate today = LocalDate.now();
        OrderDailyCounterId id = new OrderDailyCounterId(today, request.storeId());

        OrderDailyCounter counter = orderDailyCounterRepository.findById(id)
                .orElseGet(() -> new OrderDailyCounter(id, 0));

        counter.increment();
        orderDailyCounterRepository.save(counter);

        return "A-" + counter.getCount();
    }
}
