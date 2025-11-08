package ru.yandex.practicum.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.PaymentApi;

@FeignClient(name = "payment")
public interface PaymentClient extends PaymentApi {
}
