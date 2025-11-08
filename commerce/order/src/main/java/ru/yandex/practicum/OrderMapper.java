package ru.yandex.practicum;

import ru.yandex.practicum.interaction.dto.warehouse.BookedProductsDto;
import ru.yandex.practicum.interaction.dto.ShoppingCartDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.util.List;

public class OrderMapper {

    public static Order toEntity(String username, ShoppingCartDto cartDto, BookedProductsDto productsDto) {
        return Order.builder()
                .username(username)
                .shoppingCartId(cartDto.getShoppingCartId())
                .products(cartDto.getProducts())
                .deliveryWeight(productsDto.getDeliveryWeight())
                .deliveryVolume(productsDto.getDeliveryVolume())
                .fragile(productsDto.isFragile())
                .build();
    }

    public static OrderDto toDto(Order order) {
        return OrderDto.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }

    public static Order toEntity(OrderDto order) {
        return Order.builder()
                .orderId(order.getOrderId())
                .shoppingCartId(order.getShoppingCartId())
                .products(order.getProducts())
                .paymentId(order.getPaymentId())
                .deliveryId(order.getDeliveryId())
                .state(order.getState())
                .deliveryWeight(order.getDeliveryWeight())
                .deliveryVolume(order.getDeliveryVolume())
                .fragile(order.isFragile())
                .totalPrice(order.getTotalPrice())
                .deliveryPrice(order.getDeliveryPrice())
                .productPrice(order.getProductPrice())
                .build();
    }

    public static List<OrderDto> toDto(List<Order> orders) {
        return orders.stream().map(OrderMapper::toDto).toList();
    }
}
