package ru.yandex.practicum.service;

import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentService {

    PaymentDto createPayment(OrderDto orderDto);

    double getProductCost(OrderDto orderDto);

    double getTotalCost(OrderDto orderDto);

    void refund(UUID paymentId);

    void paymentFailed(UUID paymentId);
}
