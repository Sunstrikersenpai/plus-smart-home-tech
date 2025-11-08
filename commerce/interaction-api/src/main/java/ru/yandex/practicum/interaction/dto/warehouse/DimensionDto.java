package ru.yandex.practicum.interaction.dto.warehouse;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DimensionDto {
    @Positive
    private double width;

    @Positive
    private double height;

    @Positive
    private double depth;
}
