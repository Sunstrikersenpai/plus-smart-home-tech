package ru.yandex.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.interaction.api.DeliveryApi;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;
import ru.yandex.practicum.service.DeliveryService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DeliveryController implements DeliveryApi {
    private final DeliveryService deliveryService;

    @Override
    public DeliveryDto createDelivery(DeliveryDto deliveryDto) {
        return deliveryService.createDelivery(deliveryDto);
    }

    @Override
    public Double getDeliveryCost(OrderDto orderDto) {
        return deliveryService.getDeliveryCost(orderDto);
    }

    @Override
    public void setDeliverySuccessful(UUID deliveryId) {
        deliveryService.setDeliverySuccessful(deliveryId);
    }

    @Override
    public void setDeliveryFailed(UUID deliveryId) {
        deliveryService.setDeliveryFailed(deliveryId);
    }

    @Override
    public void pickOrderForDelivery(UUID deliveryId) {
        deliveryService.pickOrderForDelivery(deliveryId);
    }
}
