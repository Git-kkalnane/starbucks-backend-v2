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
import git_kkalnane.starbucksbackenv2.domain.order.service.OrderService;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import git_kkalnane.starbucksbackenv2.domain.store.dto.StoreSimpleDto;
import git_kkalnane.starbucksbackenv2.domain.store.repository.StoreRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock private OrderRepository orderRepository;
    @Mock private MemberRepository memberRepository;
    @Mock private StoreRepository storeRepository;
    @Mock private BeverageItemRepository beverageItemRepository;
    @Mock private DessertItemRepository dessertItemRepository;
    @Mock private ItemOptionRepository itemOptionRepository;
    @Mock private OrderDailyCounterRepository orderDailyCounterRepository;

    @Nested
    @DisplayName("주문 생성 테스트")
    class CreateOrderTest {

        @Test
        @DisplayName("[성공] 새로운 주문을 생성한다")
        void createOrder_Success() {
            // given
            Long memberId = 1L;
            Long storeId = 10L;

            List<SelectedItemOptionRequest> optionRequests = List.of(new SelectedItemOptionRequest(100L, 1));
            List<OrderItemRequest> orderItemRequests = List.of(
                    new OrderItemRequest(101L, ItemType.COFFEE, 2L, 1L, null, null, optionRequests),
                    new OrderItemRequest(201L, ItemType.DESSERT, 1L, null, null, null, Collections.emptyList())
            );
            OrderCreateRequest createRequest = new OrderCreateRequest(storeId, null, "test-card", null, orderItemRequests);

            Member mockMember = Member.builder().build();
            StoreSimpleDto mockStoreDto = new StoreSimpleDto(storeId, "테스트매장", null, null, null, false, 0, BigDecimal.ZERO, BigDecimal.ZERO, null, null);
            BeverageItem mockBeverage = BeverageItem.builder().price(4500).itemNameKo("아메리카노").build();
            DessertItem mockDessert = DessertItem.builder().price(6000).dessertItemNameKo("치즈케이크").build();
            ItemOption mockOption = ItemOption.builder().id(100L).optionPrice(500).build();
            OrderDailyCounter mockCounter = new OrderDailyCounter(new OrderDailyCounterId(LocalDate.now(), storeId), 0);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
            when(storeRepository.findSimpleDtoById(storeId)).thenReturn(Optional.of(mockStoreDto));
            when(beverageItemRepository.findById(101L)).thenReturn(Optional.of(mockBeverage));
            when(dessertItemRepository.findById(201L)).thenReturn(Optional.of(mockDessert));
            when(itemOptionRepository.findAllById(List.of(100L))).thenReturn(List.of(mockOption));
            when(orderDailyCounterRepository.findById(any(OrderDailyCounterId.class))).thenReturn(Optional.of(mockCounter));
            when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

            // when
            Order createdOrder = orderService.createOrder(createRequest, memberId);

            // then
            assertThat(createdOrder).isNotNull();
            assertThat(createdOrder.getTotalPrice()).isEqualTo(16000L);

            ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);
            verify(orderRepository).save(orderCaptor.capture());

            Order capturedOrder = orderCaptor.getValue();
            assertThat(capturedOrder.getOrderItems()).hasSize(2);
            assertThat(capturedOrder.getOrderItems().get(0).getOrder()).isNotNull();
            assertThat(capturedOrder.getOrderItems().get(1).getOrder()).isNotNull();
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 회원 ID로 주문을 생성하면 예외가 발생한다")
        void createOrder_Fail_MemberNotFound() {
            // given
            Long nonExistentMemberId = 999L;
            OrderCreateRequest request = new OrderCreateRequest(1L, null, null, null, Collections.emptyList());

            when(memberRepository.findById(nonExistentMemberId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.createOrder(request, nonExistentMemberId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.MEMBER_NOT_FOUND);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 매장 ID로 주문을 생성하면 예외가 발생한다")
        void createOrder_Fail_StoreNotFound() {
            // given
            Long memberId = 1L;
            Long nonExistentStoreId = 999L;
            OrderCreateRequest request = new OrderCreateRequest(nonExistentStoreId, null, null, null, Collections.emptyList());

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
            when(storeRepository.findSimpleDtoById(nonExistentStoreId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.createOrder(request, memberId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.STORE_NOT_FOUND);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 상품 ID로 주문을 생성하면 예외가 발생한다")
        void createOrder_Fail_ItemNotFound() {
            // given
            Long memberId = 1L;
            Long storeId = 1L;
            Long nonExistentItemId = 999L;

            List<OrderItemRequest> orderItemRequests = List.of(
                    new OrderItemRequest(nonExistentItemId, ItemType.COFFEE, 1L, null, null, null, null)
            );
            OrderCreateRequest request = new OrderCreateRequest(storeId, null, null, null, orderItemRequests);

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(Member.builder().build()));
            when(storeRepository.findSimpleDtoById(storeId)).thenReturn(Optional.of(new StoreSimpleDto(storeId, "테스트매장", null, null, null, false, 0, BigDecimal.ZERO, BigDecimal.ZERO, null, null)));
            when(beverageItemRepository.findById(nonExistentItemId)).thenReturn(Optional.empty());


            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.createOrder(request, memberId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.ITEM_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("사용자용 주문 상세 조회 테스트")
    class GetOrderDetailTest {

        @Test
        @DisplayName("[성공] 자신의 주문을 상세 조회한다")
        void getOrderDetail_Success() {
            // given
            Long loginMemberId = 1L;
            Long orderId = 100L;

            List<OrderRepository.OrderDetailDto> mockDtos = List.of(
                    new OrderRepository.OrderDetailDto(
                            orderId, "A-1", OrderStatus.PLACED, LocalDateTime.now(), PickupType.STORE_PICKUP, 5000L,
                            10L, "강남점", loginMemberId, "테스트유저",
                            1L, 101L, "아메리카노", 5000L, 1L, "샷 추가"
                    )
            );

            when(orderRepository.findOrderDetailDtoById(orderId)).thenReturn(mockDtos);

            // when
            OrderDetailResponse response = orderService.getOrderDetail(loginMemberId, orderId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.orderId()).isEqualTo(orderId);
            assertThat(response.memberId()).isEqualTo(loginMemberId);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 주문 ID를 조회하면 예외가 발생한다")
        void getOrderDetail_Fail_OrderNotFound() {
            // given
            Long loginMemberId = 1L;
            Long nonExistentOrderId = 999L;

            when(orderRepository.findOrderDetailDtoById(nonExistentOrderId)).thenReturn(Collections.emptyList());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getOrderDetail(loginMemberId, nonExistentOrderId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.ORDER_NOT_FOUND);
        }

        @Test
        @DisplayName("[실패] 다른 사람의 주문을 조회하면 예외가 발생한다")
        void getOrderDetail_Fail_ForbiddenAccess() {
            // given
            Long loginMemberId = 1L;
            Long orderOwnerId = 2L;
            Long orderId = 100L;

            List<OrderRepository.OrderDetailDto> mockDtos = List.of(
                    new OrderRepository.OrderDetailDto(
                            orderId, "A-1", OrderStatus.PLACED, LocalDateTime.now(), PickupType.STORE_PICKUP, 5000L,
                            10L, "강남점", orderOwnerId, "다른유저",
                            1L, 101L, "아메리카노", 5000L, 1L, "샷 추가"
                    )
            );
            when(orderRepository.findOrderDetailDtoById(orderId)).thenReturn(mockDtos);

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getOrderDetail(loginMemberId, orderId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.FORBIDDEN_ACCESS_ORDER);
        }
    }

    @Nested
    @DisplayName("사용자용 현재 주문 목록 조회 테스트")
    class GetCurrentOrdersTest {

        @Test
        @DisplayName("[성공] 현재 진행중인 주문 목록을 정상적으로 조회한다")
        void getCurrentOrders_Success() {
            // given
            Long memberId = 1L;
            Member mockMember = Member.builder().build();
            Store mockStore = Store.builder().id(10L).name("테스트매장").build();

            List<Order> mockOrders = List.of(
                    Order.builder().status(OrderStatus.PLACED).store(mockStore).build(),
                    Order.builder().status(OrderStatus.PREPARING).store(mockStore).build()
            );

            mockOrders.forEach(order -> ReflectionTestUtils.setField(order, "orderItems", new ArrayList<>()));

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
            when(orderRepository.findByMemberIdAndStatusInOrderByCreatedAtAsc(any(Long.class), any(List.class)))
                    .thenReturn(mockOrders);

            // when
            List<CustomerCurrentOrderResponse> responses = orderService.getCurrentOrders(memberId);

            // then
            assertThat(responses).isNotNull();
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).storeInfo().storeName()).isEqualTo("테스트매장");
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 회원 ID로 조회 시 예외가 발생한다")
        void getCurrentOrders_Fail_MemberNotFound() {
            // given
            Long nonExistentMemberId = 999L;

            when(memberRepository.findById(nonExistentMemberId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getCurrentOrders(nonExistentMemberId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("사용자용 과거 주문 내역 조회 테스트")
    class GetOrderHistoryTest {

        @Test
        @DisplayName("[성공] 과거 주문 내역을 페이지네이션하여 정상적으로 조회한다")
        void getOrderHistory_Success() {
            // given
            Long memberId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            Member mockMember = Member.builder().id(memberId).build();
            Store mockStore = Store.builder().id(10L).name("테스트매장").build();

            List<Order> mockOrders = List.of(
                    Order.builder().status(OrderStatus.COMPLETED).store(mockStore).build(),
                    Order.builder().status(OrderStatus.CANCELLED).store(mockStore).build()
            );
            Page<Order> mockOrderPage = new PageImpl<>(mockOrders, pageable, mockOrders.size());

            when(memberRepository.findById(memberId)).thenReturn(Optional.of(mockMember));
            when(orderRepository.findByMemberIdAndStatusIn(any(Long.class), any(List.class), any(Pageable.class)))
                    .thenReturn(mockOrderPage);

            // when
            CustomerOrderHistoryListResponse response = orderService.getOrderHistory(memberId, pageable);

            // then
            assertThat(response).isNotNull();
            assertThat(response.orders()).hasSize(2);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 회원 ID로 조회 시 예외가 발생한다")
        void getOrderHistory_Fail_MemberNotFound() {
            // given
            Long nonExistentMemberId = 999L;
            Pageable pageable = PageRequest.of(0, 10);
            when(memberRepository.findById(nonExistentMemberId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getOrderHistory(nonExistentMemberId, pageable);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("매장용 현재 주문 목록 조회 테스트")
    class GetStoreCurrentOrdersTest {

        @Test
        @DisplayName("[성공] 현재 진행중인 주문 목록을 정상적으로 조회한다")
        void getStoreCurrentOrders_Success() {
            // given
            Long storeId = 1L;
            Store mockStore = Store.builder().id(storeId).build();
            Member mockMember = Member.builder().nickname("테스트고객").build();

            List<Order> mockOrders = List.of(
                    Order.builder().status(OrderStatus.PLACED).member(mockMember).build(),
                    Order.builder().status(OrderStatus.PREPARING).member(mockMember).build()
            );

            mockOrders.forEach(order -> {
                ReflectionTestUtils.setField(order, "orderItems", new ArrayList<>());
                ReflectionTestUtils.setField(order, "store", mockStore);
            });


            when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
            when(orderRepository.findByStoreIdAndStatusInOrderByCreatedAtAsc(any(Long.class), any(List.class)))
                    .thenReturn(mockOrders);

            // when
            List<StoreCurrentOrderResponse> responses = orderService.getStoreCurrentOrders(storeId);

            // then
            assertThat(responses).isNotNull();
            assertThat(responses).hasSize(2);
            assertThat(responses.get(0).memberNickname()).isEqualTo("테스트고객");
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 매장 ID로 조회 시 예외가 발생한다")
        void getStoreCurrentOrders_Fail_StoreNotFound() {
            // given
            Long nonExistentStoreId = 999L;

            when(storeRepository.findById(nonExistentStoreId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getStoreCurrentOrders(nonExistentStoreId);
            });

            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.STORE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("매장용 주문 상세 조회 테스트")
    class GetStoreOrderDetailTest {

        @Test
        @DisplayName("[성공] 자신의 매장 주문을 상세 조회한다")
        void getStoreOrderDetail_Success() {
            // given
            Long loginStoreId = 1L;
            Long orderId = 100L;
            Long memberId = 1L;

            List<OrderRepository.OrderDetailDto> mockDtos = List.of(
                    new OrderRepository.OrderDetailDto(
                            orderId, "A-1", OrderStatus.PLACED, LocalDateTime.now(), PickupType.STORE_PICKUP, 5000L,
                            loginStoreId, "강남점", memberId, "테스트유저",
                            1L, 101L, "아메리카노", 5000L, 1L, "샷 추가"
                    )
            );
            when(orderRepository.findOrderDetailDtoById(orderId)).thenReturn(mockDtos);

            // when
            OrderDetailResponse response = orderService.getStoreOrderDetail(loginStoreId, orderId);

            // then
            assertThat(response).isNotNull();
            assertThat(response.orderId()).isEqualTo(orderId);
            assertThat(response.storeId()).isEqualTo(loginStoreId);
            assertThat(response.memberId()).isNull();
            assertThat(response.memberNickname()).isEqualTo("테스트유저");
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 주문 ID를 조회하면 예외가 발생한다")
        void getStoreOrderDetail_Fail_OrderNotFound() {
            // given
            Long loginStoreId = 1L;
            Long nonExistentOrderId = 999L;
            when(orderRepository.findOrderDetailDtoById(nonExistentOrderId)).thenReturn(Collections.emptyList());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getStoreOrderDetail(loginStoreId, nonExistentOrderId);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.ORDER_NOT_FOUND);
        }

        @Test
        @DisplayName("[실패] 다른 매장의 주문을 조회하면 예외가 발생한다")
        void getStoreOrderDetail_Fail_ForbiddenAccess() {
            // given
            Long loginStoreId = 1L;
            Long orderStoreId = 2L;
            Long orderId = 100L;
            Long memberId = 1L;

            List<OrderRepository.OrderDetailDto> mockDtos = List.of(
                    new OrderRepository.OrderDetailDto(
                            orderId, "A-1", OrderStatus.PLACED, LocalDateTime.now(), PickupType.STORE_PICKUP, 5000L,
                            orderStoreId, "서초점", memberId, "테스트유저",
                            1L, 101L, "아메리카노", 5000L, 1L, null
                    )
            );
            when(orderRepository.findOrderDetailDtoById(orderId)).thenReturn(mockDtos);

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getStoreOrderDetail(loginStoreId, orderId);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.FORBIDDEN_ACCESS_ORDER);
        }
    }

    @Nested
    @DisplayName("매장용 과거 주문 내역 조회 테스트")
    class GetStoreOrderHistoryTest {

        @Test
        @DisplayName("[성공] 과거 주문 내역을 페이지네이션하여 정상적으로 조회한다")
        void getStoreOrderHistory_Success() {
            // given
            Long storeId = 1L;
            Pageable pageable = PageRequest.of(0, 10);
            Store mockStore = Store.builder().id(storeId).build();
            Member mockMember = Member.builder().nickname("테스트고객").build();

            List<Order> mockOrders = List.of(
                    Order.builder().status(OrderStatus.COMPLETED).store(mockStore).member(mockMember).build(),
                    Order.builder().status(OrderStatus.CANCELLED).store(mockStore).member(mockMember).build()
            );

            mockOrders.forEach(order -> ReflectionTestUtils.setField(order, "orderItems", new ArrayList<>()));

            Page<Order> mockOrderPage = new PageImpl<>(mockOrders, pageable, mockOrders.size());

            when(storeRepository.findById(storeId)).thenReturn(Optional.of(mockStore));
            when(orderRepository.findByStoreIdAndStatusIn(any(Long.class), any(List.class), any(Pageable.class)))
                    .thenReturn(mockOrderPage);

            // when
            StoreOrderHistoryListResponse response = orderService.getStoreOrderHistory(storeId, pageable);

            // then
            assertThat(response).isNotNull();
            assertThat(response.orders()).hasSize(2);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 매장 ID로 조회 시 예외가 발생한다")
        void getStoreOrderHistory_Fail_StoreNotFound() {
            // given
            Long nonExistentStoreId = 999L;
            Pageable pageable = PageRequest.of(0, 10);
            when(storeRepository.findById(nonExistentStoreId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.getStoreOrderHistory(nonExistentStoreId, pageable);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.STORE_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("매장용 주문 상태 변경 테스트")
    class UpdateOrderStatusTest {

        private Order mockOrder;
        private Store mockStore;
        private Long storeId = 1L;
        private Long orderId = 100L;

        @BeforeEach
        void setUp() {
            mockStore = Store.builder().id(storeId).build();
            mockOrder = Order.builder()
                    .id(orderId)
                    .store(mockStore)
                    .status(OrderStatus.PLACED)
                    .build();
        }

        @Test
        @DisplayName("[성공] 주문 상태를 정상적으로 변경한다")
        void updateOrderStatus_Success() {
            // given
            OrderStatus newStatus = OrderStatus.PREPARING;
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

            // when
            orderService.updateOrderStatus(storeId, orderId, newStatus);

            // then
            assertThat(mockOrder.getStatus()).isEqualTo(newStatus);
        }

        @Test
        @DisplayName("[실패] 존재하지 않는 주문의 상태를 변경하려하면 예외가 발생한다")
        void updateOrderStatus_Fail_OrderNotFound() {
            // given
            when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.updateOrderStatus(storeId, orderId, OrderStatus.PREPARING);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.ORDER_NOT_FOUND);
        }

        @Test
        @DisplayName("[실패] 다른 매장의 주문 상태를 변경하려하면 예외가 발생한다")
        void updateOrderStatus_Fail_Forbidden() {
            // given
            Long otherStoreId = 2L;
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.updateOrderStatus(otherStoreId, orderId, OrderStatus.PREPARING);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.FORBIDDEN_ACCESS_ORDER);
        }

        @Test
        @DisplayName("[실패] 이미 완료(COMPLETED)된 주문 상태를 변경하려하면 예외가 발생한다")
        void updateOrderStatus_Fail_CannotUpdateCompletedOrder() {
            // given
            mockOrder.updateStatus(OrderStatus.COMPLETED);
            when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));

            // when & then
            OrderException exception = assertThrows(OrderException.class, () -> {
                orderService.updateOrderStatus(storeId, orderId, OrderStatus.PREPARING);
            });
            assertThat(exception.getErrorCode()).isEqualTo(OrderErrorCode.CANNOT_UPDATE_COMPLETED_ORDER);
        }
    }

}