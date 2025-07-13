package git_kkalnane.starbucksbackenv2.domain.order.dto.response;

import git_kkalnane.starbucksbackenv2.domain.item.domain.ItemOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageItem;
import git_kkalnane.starbucksbackenv2.domain.item.domain.beverage.BeverageTemperatureOption;
import git_kkalnane.starbucksbackenv2.domain.item.domain.dessert.DessertItem;
import git_kkalnane.starbucksbackenv2.domain.order.domain.Order;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItem;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderItemOption;
import git_kkalnane.starbucksbackenv2.domain.order.domain.OrderStatus;
import git_kkalnane.starbucksbackenv2.domain.order.domain.PickupType;
import git_kkalnane.starbucksbackenv2.domain.store.domain.Store;
import java.util.List;
import java.util.Map;

/**
 * [고객용] 주문 상세 조회를 위한 응답 DTO.
 */
public record CustomerOrderDetailResponse(
        int totalCount,
        String orderNumber,
        StoreInfo storeInfo,
        OrderStatus orderStatus,
        PickupType pickupType,
        List<OrderItemDetail> orderItems,
        Long totalPrice
) {
    public static CustomerOrderDetailResponse of(Order order, Map<Long, BeverageItem> beverageMap, Map<Long, DessertItem> dessertMap, Map<Long, ItemOption> optionMap) {
        return new CustomerOrderDetailResponse(
                order.getOrderItems().size(),
                order.getOrderNumber(),
                StoreInfo.from(order.getStore()),
                order.getStatus(),
                order.getPickupType(),
                order.getOrderItems().stream().map(orderItem -> OrderItemDetail.of(orderItem, beverageMap, dessertMap, optionMap)).toList(),
                order.getTotalPrice()
        );
    }

    private record StoreInfo(Long storeId, String storeName, String storeAddress) {
        public static StoreInfo from(Store store) {
            return new StoreInfo(store.getId(), store.getName(), store.getAddress());
        }
    }

    private record OrderItemDetail(Long itemId, String itemName, List<OptionDetail> options, Long itemPrice, Integer quantity, String imgUrl) {
        public static OrderItemDetail of(OrderItem orderItem, Map<Long, BeverageItem> beverageMap, Map<Long, DessertItem> dessertMap, Map<Long, ItemOption> optionMap) {
            String imageUrl = null;
            Long itemId;

            if (orderItem.getBeverageItemId() != null) {
                itemId = orderItem.getBeverageItemId();
                BeverageItem beverage = beverageMap.get(itemId);
                if (beverage != null) {
                    imageUrl = (orderItem.getSelectedTemperature() == BeverageTemperatureOption.HOT)
                            ? beverage.getHotImageUrl() : beverage.getIceImageUrl();
                }
            } else {
                itemId = orderItem.getDessertItemId();
                DessertItem dessert = dessertMap.get(itemId);
                if (dessert != null) {
                    imageUrl = dessert.getImageUrl();
                }
            }

            return new OrderItemDetail(
                    itemId,
                    orderItem.getItemName(),
                    orderItem.getOrderItemOptions().stream().map(opt -> OptionDetail.from(opt, optionMap)).toList(),
                    orderItem.getFinalItemPrice(),
                    orderItem.getQuantity(),
                    imageUrl
            );
        }
    }

    private record OptionDetail(int quantity, String optionName, Integer optionPrice) {
        public static OptionDetail from(OrderItemOption orderItemOption, Map<Long, ItemOption> optionMap) {
            ItemOption option = optionMap.get(orderItemOption.getItemOptionId());
            return new OptionDetail(
                    orderItemOption.getQuantity(),
                    option != null ? option.getName() : "알 수 없는 옵션",
                    option != null ? option.getOptionPrice() : 0
            );
        }
    }
}