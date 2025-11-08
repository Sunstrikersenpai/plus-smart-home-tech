package ru.yandex.practicum.interaction.dto.warehouse;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class ChangeProductQuantityRequest {
    private UUID productId;

    @PositiveOrZero
    private long newQuantity;
}
