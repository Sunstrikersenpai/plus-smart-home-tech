package ru.yandex.practicum.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.Payment;
import ru.yandex.practicum.PaymentMapper;
import ru.yandex.practicum.PaymentRepository;
import ru.yandex.practicum.PaymentState;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;
import ru.yandex.practicum.interaction.exeception.NoOrderFoundException;
import ru.yandex.practicum.interaction.exeception.NotEnoughInfoInOrderToCalculateException;

import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final ShoppingStoreClient shoppingStoreClient;
    private final OrderClient orderClient;

    @Override
    public double getProductCost(OrderDto orderDto) {
        validateOrderDto(orderDto);

        double total = 0;
        for (Map.Entry<UUID, Long> entry : orderDto.getProducts().entrySet()) {
            UUID productId = entry.getKey();
            long quantity = entry.getValue();
            Float price = shoppingStoreClient.getProduct(productId).getPrice();
            total += price * quantity;
        }
        return total;
    }

    @Override
    public double getTotalCost(OrderDto orderDto) {
        validateOrderDto(orderDto);
        double products = getProductCost(orderDto);
        double delivery = orderDto.getDeliveryPrice();
        return products + (products * 0.1) + delivery;
    }

    @Override
    public PaymentDto createPayment(OrderDto orderDto) {
        validateOrderDto(orderDto);
        double productCost = getProductCost(orderDto);
        double delivery = orderDto.getDeliveryPrice();
        double total = productCost + (productCost * 0.1) + delivery;

        Payment payment = Payment.builder()
                .orderId(orderDto.getOrderId())
                .totalPayment(total)
                .deliveryTotal(delivery)
                .feeTotal(productCost * 0.1)
                .paymentState(PaymentState.PENDING)
                .build();

        paymentRepository.save(payment);
        return paymentMapper.toDto(payment);
    }

    @Override
    public void refund(UUID paymentId) {
        Payment payment = getExistingPayment(paymentId);
        payment.setPaymentState(PaymentState.SUCCESS);
        paymentRepository.save(payment);

        orderClient.payment(payment.getOrderId());
    }

    @Override
    public void paymentFailed(UUID paymentId) {
        Payment payment = getExistingPayment(paymentId);
        payment.setPaymentState(PaymentState.FAILED);
        paymentRepository.save(payment);

        orderClient.paymentFailed(payment.getOrderId());
    }

    private Payment getExistingPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .orElseThrow(() -> new NoOrderFoundException("Payment not found: " + paymentId));
    }

    private void validateOrderDto(OrderDto dto) {
        if (dto == null) {
            throw new NotEnoughInfoInOrderToCalculateException("OrderDto is null");
        }
        if (dto.getOrderId() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("Order ID must not be null");
        }
        if (dto.getProducts() == null || dto.getProducts().isEmpty()) {
            throw new NotEnoughInfoInOrderToCalculateException("Product list must not be empty");
        }
        if (dto.getShoppingCartId() == null) {
            throw new NotEnoughInfoInOrderToCalculateException("ShoppingCartId must not be null");
        }
    }
}
