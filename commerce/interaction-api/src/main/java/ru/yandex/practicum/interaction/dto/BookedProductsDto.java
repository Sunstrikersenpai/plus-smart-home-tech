package ru.yandex.practicum.interaction.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookedProductsDto {
    @PositiveOrZero
    private double deliveryWeight;

    @PositiveOrZero
    private double deliveryVolume;

    private boolean fragile;
}