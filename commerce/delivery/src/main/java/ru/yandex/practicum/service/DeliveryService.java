package ru.yandex.practicum.service;

import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryService {
    DeliveryDto createDelivery(DeliveryDto request);

    double getDeliveryCost(OrderDto orderDto);

    void pickOrderForDelivery(UUID orderId);

    void setDeliverySuccessful(UUID orderId);

    void setDeliveryFailed(UUID orderId);
}