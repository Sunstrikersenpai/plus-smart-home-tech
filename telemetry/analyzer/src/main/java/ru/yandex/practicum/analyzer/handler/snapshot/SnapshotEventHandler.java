package ru.yandex.practicum.analyzer.handler.snapshot;

import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.model.enums.SensorType;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;

import java.time.Instant;

public interface SnapshotEventHandler {
    SensorType getType();

    Integer extractValue(SensorStateAvro state, ConditionType conditionType);

    void handle(String hubId, String sensorId, Instant timestamp, SensorStateAvro state);
}