package ru.yandex.practicum.analyzer.handler.snapshot;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.analyzer.model.enums.ConditionType;
import ru.yandex.practicum.analyzer.model.enums.SensorType;
import ru.yandex.practicum.analyzer.service.SnapshotAnalysisService;
import ru.yandex.practicum.kafka.telemetry.event.SensorStateAvro;
import ru.yandex.practicum.kafka.telemetry.event.TemperatureSensorAvro;

import java.time.Instant;

@AllArgsConstructor
@Component
public class TemperatureSensorHandler implements SnapshotEventHandler{
    private final SnapshotAnalysisService analysisService;

    @Override
    public SensorType getType() {
        return SensorType.TEMPERATURE_SENSOR;
    }

    @Override
    public Integer extractValue(SensorStateAvro state, ConditionType conditionType) {
        TemperatureSensorAvro data = (TemperatureSensorAvro) state.getData();

        return data.getInttemperatureC();
    }

    @Override
    public void handle(String hubId, String sensorId, Instant timestamp, SensorStateAvro state) {
        Integer value = extractValue(state, ConditionType.TEMPERATURE);
        if (value != null) {
            analysisService.processSensor(hubId, sensorId, value);
        }
    }
}
