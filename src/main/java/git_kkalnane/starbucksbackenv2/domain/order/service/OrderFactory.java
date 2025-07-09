package git_kkalnane.starbucksbackenv2.domain.order.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.order.common.exception.OrderErrorCode;
import git_kkalnane.starbucksbackenv2.domain.order.common.exception.OrderException;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItem;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItemOption;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderCreateRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderItemRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.SelectedItemOptionRequest;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFactory {
    private final BeverageItemRepository beverageItemRepository;
    private final DessertItemRepository dessertItemRepository;
    private final ItemOptionRepository itemOptionRepository;

    /**
     * OrderCreateRequest를 받아 각 주문 아이템 요청을 OrderItem 엔티티 리스트로 변환
     *
     * @param request 주문 생성 요청(여러 주문 아이템 포함)
     * @return 생성된 OrderItem 엔티티 리스트
     */
    public List<OrderItem> createOrderItems(OrderCreateRequest request) {
        return request.orderItems().stream()
            .map(this::createOrderItemFromRequest)
            .collect(Collectors.toList());
    }

    /**
     * 단일 OrderItemRequest로부터 OrderItem 엔티티 생성 아이템 타입, 가격 계산, 옵션 매핑 처리
     *
     * @param request 주문 아이템 요청
     * @return 생성된 OrderItem 엔티티
     */
    public OrderItem createOrderItemFromRequest(OrderItemRequest request) {
        // 1. 아이템 가격 및 이름 결정
        ItemInfo itemInfo = getItemInfo(request);

        // 2. 옵션 가격 계산
        long optionsTotalPrice = calculateOptionsTotalPrice(request);

        // 3. 최종 금액 계산
        long finalItemPrice = (itemInfo.itemPrice + optionsTotalPrice) * request.quantity();

        // 4. 옵션 리스트 생성
        List<OrderItemOption> options = createOrderItemOptions(request);

        // 5. OrderItem 생성 (정적 팩토리 메서드 활용)
        return OrderItem.ofRequest(
            request,
            itemInfo.itemName,
            itemInfo.itemPrice,
            finalItemPrice,
            options
        );
    }

    // 아이템 정보(이름, 가격) 추출
    private ItemInfo getItemInfo(OrderItemRequest request) {
        if (request.itemType() == ItemType.BEVERAGE || request.itemType() == ItemType.COFFEE) {
            BeverageItem item = beverageItemRepository.findByIdWithSupportedSizes(request.itemId())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ITEM_NOT_FOUND));
            return new ItemInfo(item.getItemNameKo(), item.getPrice());
        } else {
            DessertItem item = dessertItemRepository.findById(request.itemId())
                .orElseThrow(() -> new OrderException(OrderErrorCode.ITEM_NOT_FOUND));
            return new ItemInfo(item.getDessertItemNameKo(), item.getPrice());
        }
    }

    // 옵션 총 가격 계산
    private long calculateOptionsTotalPrice(OrderItemRequest request) {
        if (request.options() == null || request.options().isEmpty()) {
            return 0L;
        }
        List<Long> optionIds = request.options().stream().map(SelectedItemOptionRequest::itemOptionId).toList();
        Map<Long, ItemOption> itemOptionsMap = itemOptionRepository.findAllById(optionIds).stream()
            .collect(Collectors.toMap(ItemOption::getId, option -> option));
        return request.options().stream().mapToLong(optReq -> {
            ItemOption option = itemOptionsMap.get(optReq.itemOptionId());
            if (option == null) {
                throw new OrderException(OrderErrorCode.ITEM_OPTION_NOT_FOUND);
            }
            return (long) option.getOptionPrice() * optReq.quantity();
        }).sum();
    }

    // 옵션 엔티티 리스트 생성
    private List<OrderItemOption> createOrderItemOptions(OrderItemRequest request) {
        if (request.options() == null) {
            return List.of();
        }
        return request.options().stream()
            .map(option -> OrderItemOption.builder()
                .itemOptionId(option.itemOptionId())
                .quantity(option.quantity())
                .build())
            .collect(Collectors.toList());
    }

    /**
     * StoreSimpleDto의 ID만 활용하여 최소한의 Store 엔티티 생성
     *
     * @param storeDto 매장 ID 정보가 담긴 DTO
     * @return ID만 설정된 Store 엔티티
     */
    public Store createMinimalStore(StoreSimpleDto storeDto) {
        return Store.builder()
            .id(storeDto.getId())
            .build();
    }

    /**
     * 주문 생성에 필요한 회원, 매장, 주문번호, 총금액, 요청정보를 받아 Order 엔티티 생성
     *
     * @param member      주문하는 회원
     * @param store       주문이 이루어지는 매장
     * @param orderNumber 생성된 주문번호
     * @param totalPrice  계산된 총 주문 금액
     * @param request     주문 생성 요청
     * @return 생성된 Order 엔티티
     */
    public Order createOrder(Member member, Store store, String orderNumber, Long totalPrice,
                             OrderCreateRequest request) {
        return Order.createNewOrder(member, store, orderNumber, totalPrice, request);
    }

    // 아이템 정보 보조 클래스
    private static class ItemInfo {

        String itemName;
        long itemPrice;

        ItemInfo(String itemName, long itemPrice) {
            this.itemName = itemName;
            this.itemPrice = itemPrice;
        }
    }
}
