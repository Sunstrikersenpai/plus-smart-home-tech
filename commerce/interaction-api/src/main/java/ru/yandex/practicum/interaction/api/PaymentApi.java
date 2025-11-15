package ru.yandex.practicum.interaction.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.util.UUID;

public interface PaymentApi {
    @PostMapping("/api/v1/payment")
    PaymentDto createPayment(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/productCost")
    Double getProductCost(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/totalCost")
    Double getTotalCost(@RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/payment/failed")
    void setPaymentFailed(@RequestBody UUID paymentId);

    @PostMapping("/api/v1/payment/refund")
    void payOrder(@RequestBody UUID paymentId);
}