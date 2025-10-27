package ru.yandex.practicum.analyzer.handler.hub;

import ru.yandex.practicum.analyzer.model.enums.HubEventType;
import ru.yandex.practicum.kafka.telemetry.event.HubEventAvro;

public interface HubEventHandler {
    HubEventType getType();

    void handle(HubEventAvro event);
}
