package ru.yandex.practicum;

import org.mapstruct.Mapper;
import ru.yandex.practicum.interaction.dto.payment.PaymentDto;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDto toDto(Payment entity);

    Payment toEntity(PaymentDto dto);
}
