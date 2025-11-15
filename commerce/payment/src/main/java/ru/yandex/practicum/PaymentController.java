package ru.yandex.practicum;

import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.PaymentApi;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

import java.util.UUID;

@RestController
public class PaymentController implements PaymentApi {
    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        return null;
    }

    @Override
    public Double getProductCost(OrderDto orderDto) {
        return 0.0;
    }

    @Override
    public Double getTotalCost(OrderDto orderDto) {
        return 0.0;
    }

    @Override
    public void setPaymentFailed(UUID paymentId) {

    }

    @Override
    public void payOrder(UUID paymentId) {

    }
}
