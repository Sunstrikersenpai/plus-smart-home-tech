package ru.yandex.practicum.interaction.dto.warehouse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShippedToDeliveryRequest {
    private UUID orderId;
    private UUID deliveryId;
}