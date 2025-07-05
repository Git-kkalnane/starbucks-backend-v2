package git_kkalnane.starbucksbackenv2.domain.order.service;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemType;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.item.repository.BeverageItemRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.DessertItemRepository;
import git_kkalnane.starbucksbackenv2.domain.item.repository.ItemOptionRepository;
import git_kkalnane.starbucksbackenv2.domain.member.domain.Member;
import git_kkalnane.starbucksbackenv2.domain.member.repository.MemberRepository;
import git_kkalnane.starbucksbackenv2.domain.order.common.exception.OrderErrorCode;
import git_kkalnane.starbucksbackenv2.domain.order.common.exception.OrderException;
import git_kkalnane.starbucksbackenv2.domain.order.domain.*;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderCreateRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.OrderItemRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.request.SelectedItemOptionRequest;
import git_kkalnane.starbucksbackenv2.domain.order.dto.response.*;
import git_kkalnane.starbucksbackenv2.domain.order.repository.OrderDailyCounterRepository;
import git_kkalnane.starbucksbackenv2.domain.order.repository.OrderRepository;
//import git_kkalnane.starbucksbackenv2.domain.payment.service.PaymentService;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.store.repository.StoreRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 주문 관련 비즈니스 로직을 처리하는 서비스 클래스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final OrderItemService orderItemService;
    private final OrderItemOptionService orderItemOptionService;
    private final StoreRepository storeRepository;
    private final BeverageItemRepository beverageItemRepository;
    private final DessertItemRepository dessertItemRepository;
    private final ItemOptionRepository itemOptionRepository;
    private final OrderDailyCounterRepository orderDailyCounterRepository;

    // ====== 주문 생성 ======
    /**
     * 새로운 주문을 생성하고, 가격을 계산하며, 결제를 처리합니다.
     *
     * @param request  주문 생성에 필요한 모든 정보를 담은 DTO
     * @param memberId 주문을 생성하는 회원의 ID (인증을 통해 획득)
     * @return 생성되고 저장된 Order 엔티티
     */
    @Transactional
    public Order createOrder(OrderCreateRequest request, Long memberId) {
        Member member = getMemberOrThrow(memberId);
        StoreSimpleDto store = getStoreDtoOrThrow(request.storeId());
        List<OrderItem> orderItems = createOrderItems(request);
        Long calculatedTotalPrice = calculateTotalPrice(orderItems);
        String orderNumber = generateOrderNumber(request);
        Store minimalStore = createMinimalStore(store);
        Order order = Order.createNewOrder(member, minimalStore, orderNumber, calculatedTotalPrice,request);
        // paymentService.processPayment(savedOrder);

        // Order 저장
        Order savedOrder = orderRepository.save(order);

        // List<OrderItem> 저장, OrderItem은 OrderId가 없으면 에러 발생
        orderItemService.saveOrderItems(savedOrder.getId(), orderItems);

        return savedOrder;
    }


    // ====== Private Helper Methods ======
    private Member getMemberOrThrow(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.MEMBER_NOT_FOUND));
    }

    private StoreSimpleDto getStoreDtoOrThrow(Long storeId) {
        return storeRepository.findSimpleDtoById(storeId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));
    }

    private List<OrderItem> createOrderItems(OrderCreateRequest request) {
        return request.orderItems().stream()
                .map(this::createOrderItemFromRequest)
                .collect(Collectors.toList());
    }

    private Long calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToLong(OrderItem::getFinalItemPrice)
                .sum();
    }

    private Store createMinimalStore(StoreSimpleDto storeDto) {
        return Store.builder()
                .id(storeDto.getId())
                .build();
    }




    /**
     * OrderItemRequest DTO로부터 OrderItem 엔티티를 생성하고 가격을 계산합니다.
     */
    private OrderItem createOrderItemFromRequest(OrderItemRequest request) {
        long itemPrice = 0L;
        String itemName = "";

        if (request.itemType() == ItemType.BEVERAGE || request.itemType() == ItemType.COFFEE) {
            BeverageItem item = beverageItemRepository.findById(request.itemId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.ITEM_NOT_FOUND));
            itemPrice = item.getPrice();
            itemName = item.getItemNameKo();
        } else { // DESSERT
            DessertItem item = dessertItemRepository.findById(request.itemId())
                    .orElseThrow(() -> new OrderException(OrderErrorCode.ITEM_NOT_FOUND));
            itemPrice = item.getPrice();
            itemName = item.getDessertItemNameKo();
        }

        long optionsTotalPrice = 0L;
        if (request.options() != null && !request.options().isEmpty()) {
            List<Long> optionIds = request.options().stream().map(SelectedItemOptionRequest::itemOptionId).toList();
            Map<Long, ItemOption> itemOptionsMap = itemOptionRepository.findAllById(optionIds).stream()
                    .collect(Collectors.toMap(ItemOption::getId, option -> option));

            optionsTotalPrice = request.options().stream().mapToLong(optReq -> {
                ItemOption option = itemOptionsMap.get(optReq.itemOptionId());
                if (option == null) throw new OrderException(OrderErrorCode.ITEM_OPTION_NOT_FOUND);
                return (long) option.getOptionPrice() * optReq.quantity();
            }).sum();
        }

        long finalItemPrice = (itemPrice + optionsTotalPrice) * request.quantity();

        List<OrderItemOption> options=  request.options().stream().map(
            (option) -> {return OrderItemOption.builder()
                .itemOptionId(option.itemOptionId())
                .quantity(option.quantity())
                .build();
            }
        ).collect(Collectors.toList());

        return OrderItem.builder()
                .itemType(request.itemType())
                .beverageItemId(request.itemType() != ItemType.DESSERT ? request.itemId() : null)
                .dessertItemId(request.itemType() == ItemType.DESSERT ? request.itemId() : null)
                .quantity( request.quantity())
                .itemName(itemName)
                .itemPrice(itemPrice)
                .finalItemPrice(finalItemPrice)
                .shotQuantity(request.shotQuantity())
                .selectedSize(request.selectedSizes())
                .selectedTemperature(request.selectedTemperatures())
                .orderItemOptions(options)
                .build();
    }

    /**
     * 주문번호를 생성합니다.
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

    /**
     * [사용자용] 주문 상세 정보를 조회합니다.
     */
    public CustomerOrderDetailResponse getOrderDetail(Long loginMemberId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.getMember().getId().equals(loginMemberId)) {
            throw new OrderException(OrderErrorCode.FORBIDDEN_ACCESS_ORDER);
        }

        List<Long> beverageIds = order.getOrderItems().stream()
                .filter(oi -> oi.getBeverageItemId() != null)
                .map(OrderItem::getBeverageItemId).distinct().toList();
        List<Long> dessertIds = order.getOrderItems().stream()
                .filter(oi -> oi.getDessertItemId() != null)
                .map(OrderItem::getDessertItemId).distinct().toList();
        List<Long> optionIds = order.getOrderItems().stream()
                .flatMap(oi -> oi.getOrderItemOptions().stream())
                .map(OrderItemOption::getItemOptionId).distinct().toList();

        Map<Long, BeverageItem> beverageMap = beverageItemRepository.findAllById(beverageIds).stream()
                .collect(Collectors.toMap(BeverageItem::getId, item -> item));
        Map<Long, DessertItem> dessertMap = dessertItemRepository.findAllById(dessertIds).stream()
                .collect(Collectors.toMap(DessertItem::getId, item -> item));
        Map<Long, ItemOption> optionMap = itemOptionRepository.findAllById(optionIds).stream()
                .collect(Collectors.toMap(ItemOption::getId, option -> option));

        return CustomerOrderDetailResponse.of(order, beverageMap, dessertMap, optionMap);
    }

    /**
     * [사용자용] 현재 진행중인 주문 목록(PLACED, PREPARING, READY_FOR_PICKUP)을 조회합니다.
     *
     * @param memberId 조회할 사용자의 ID
     * @return 현재 주문 목록에 대한 요약 DTO 리스트
     */
    public List<CustomerCurrentOrderResponse> getCurrentOrders(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.MEMBER_NOT_FOUND));

        List<OrderStatus> currentStatuses = List.of(
                OrderStatus.PLACED,
                OrderStatus.PREPARING,
                OrderStatus.READY_FOR_PICKUP
        );

        List<Order> currentOrders = orderRepository.findByMemberIdAndStatusInOrderByCreatedAtAsc(memberId, currentStatuses);

        return currentOrders.stream()
                .map(CustomerCurrentOrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * [사용자용] 특정 회원의 과거 주문 내역(완료, 취소)을 페이지네이션하여 조회합니다.
     *
     * @param memberId 조회할 회원의 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 페이지네이션된 과거 주문 내역 DTO
     */
    public CustomerOrderHistoryListResponse getOrderHistory(Long memberId, Pageable pageable) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.MEMBER_NOT_FOUND));

        List<OrderStatus> pastStatuses = List.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED);

        Page<Order> orderPage = orderRepository.findByMemberIdAndStatusIn(memberId, pastStatuses, pageable);

        return CustomerOrderHistoryListResponse.from(orderPage);
    }

    /**
     * [매장용] 특정 매장의 현재 진행중인 모든 주문 목록(접수, 준비중, 픽업 가능)을 조회합니다. <br/>
     * @param storeId 조회할 매장의 ID
     * @return 현재 진행중인 주문 목록 DTO 리스트
     */
    public List<StoreCurrentOrderResponse> getStoreCurrentOrders(Long storeId) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));

        List<OrderStatus> currentStatuses = List.of(
                OrderStatus.PLACED,
                OrderStatus.PREPARING,
                OrderStatus.READY_FOR_PICKUP
        );

        List<Order> currentOrders = orderRepository.findByStoreIdAndStatusInOrderByCreatedAtAsc(storeId, currentStatuses);

        return currentOrders.stream()
                .map(StoreCurrentOrderResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * [매장용] 특정 주문의 상세 정보를 조회합니다. 이 때, 요청한 매장이 해당 주문을 조회할 권한이 있는지 확인합니다. <br/>
     *
     * @param loginStoreId 로그인한 매장의 ID
     * @param orderId      조회할 주문의 ID
     * @return 조회된 Order 엔티티
     */
    public Order getStoreOrderDetail(Long loginStoreId, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.getStore().getId().equals(loginStoreId)) {
            throw new OrderException(OrderErrorCode.FORBIDDEN_ACCESS_ORDER);
        }

        return order;
    }

    /**
     * [매장용] 특정 매장의 과거 주문 내역(완료, 취소)을 페이지네이션하여 조회합니다. <br/>
     * @param storeId  조회할 매장의 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 페이지네이션된 과거 주문 내역 DTO
     */
    public StoreOrderHistoryListResponse getStoreOrderHistory(Long storeId, Pageable pageable) {
        storeRepository.findById(storeId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.STORE_NOT_FOUND));

        List<OrderStatus> pastStatuses = List.of(OrderStatus.COMPLETED, OrderStatus.CANCELLED);

        Page<Order> orderPage = orderRepository.findByStoreIdAndStatusIn(storeId, pastStatuses, pageable);

        return StoreOrderHistoryListResponse.from(orderPage);
    }

    /**
     * [매장용] 특정 주문의 상태를 변경합니다.
     *
     * @param storeId   요청한 매장의 ID (권한 검증용)
     * @param orderId   상태를 변경할 주문의 ID
     * @param newStatus 변경할 새로운 주문 상태
     */
    @Transactional
    public void updateOrderStatus(Long storeId, Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderException(OrderErrorCode.ORDER_NOT_FOUND));

        if (!order.getStore().getId().equals(storeId)) {
            throw new OrderException(OrderErrorCode.FORBIDDEN_ACCESS_ORDER);
        }

        if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new OrderException(OrderErrorCode.CANNOT_UPDATE_COMPLETED_ORDER);
        }

        order.updateStatus(newStatus);
    }
}