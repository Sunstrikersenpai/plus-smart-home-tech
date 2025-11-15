package ru.yandex.practicum.service;

import org.springframework.cloud.openfeign.FeignClient;
import ru.yandex.practicum.interaction.api.OrderApi;

@FeignClient(name = "order")
public interface OrderClient extends OrderApi {
}
