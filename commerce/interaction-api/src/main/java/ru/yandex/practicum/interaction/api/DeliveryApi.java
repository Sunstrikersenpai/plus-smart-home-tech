package ru.yandex.practicum.interaction.api;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.interaction.dto.delivery.DeliveryDto;
import ru.yandex.practicum.interaction.dto.order.OrderDto;

import java.util.UUID;

public interface DeliveryApi {
    @PutMapping("/api/v1/delivery")
    DeliveryDto createDelivery(@Valid @RequestBody DeliveryDto deliveryDto);

    @PostMapping("/api/v1/delivery/cost")
    Double getDeliveryCost(@Valid @RequestBody OrderDto orderDto);

    @PostMapping("/api/v1/delivery/successful")
    void setDeliverySuccessful(@RequestBody UUID deliveryId);

    @PostMapping("/api/v1/delivery/failed")
    void setDeliveryFailed(@RequestBody UUID deliveryId);

    @PostMapping("/api/v1/delivery/picked")
    void pickOrderForDelivery(@RequestBody UUID deliveryId);
}
